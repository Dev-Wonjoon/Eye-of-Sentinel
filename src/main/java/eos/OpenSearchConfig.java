package eos;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLContextSpi;
import java.net.URISyntaxException;

@Configuration
public class OpenSearchConfig {
    @Value("${EOS_OPENSEARCH_URI}")
    private String uris;

    @Value("${EOS_OPENSEARCH_USERNAME}")
    private String username;

    @Value("${EOS_OPENSEARCH_PASSWORD}")
    private String password;

    @Bean
    public OpenSearchClient openSearchClient() throws Exception {
        final HttpHost host = HttpHost.create(uris);
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(host),
                new UsernamePasswordCredentials(username, password.toCharArray())
        );
        final SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true)
                .build();
        final OpenSearchTransport transport = ApacheHttpClient5TransportBuilder.builder(host)
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    final var tlsStrategy = ClientTlsStrategyBuilder.create()
                            .setSslContext(sslContext)
                            .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                            .buildAsync();
                    final PoolingAsyncClientConnectionManager connectionManager = PoolingAsyncClientConnectionManagerBuilder.create()
                            .setTlsStrategy(tlsStrategy)
                            .build();
                    return httpClientBuilder
                            .setConnectionManager(connectionManager)
                            .setDefaultCredentialsProvider(credentialsProvider);
                })
                .build();
        return new OpenSearchClient(transport);
    }
}
