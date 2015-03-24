package com.segment.analytics;

import com.segment.analytics.TestUtils.MessageBuilder;
import com.segment.analytics.internal.AnalyticsClient;
import com.segment.analytics.messages.Message;
import com.squareup.burst.BurstJUnit4;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(BurstJUnit4.class) public class AnalyticsTest {

  @Mock AnalyticsClient client;
  MessageInterceptor interceptor;
  Analytics analytics;

  @Before public void setUp() {
    initMocks(this);
    MessageInterceptor realInterceptor = new MessageInterceptor() {
      @Override public Message intercept(Message message) {
        return message;
      }
    };
    interceptor = Mockito.spy(realInterceptor);
    analytics = new Analytics(client, Collections.singletonList(interceptor));
  }

  @Test public void enqueueIsDispatched(MessageBuilder builder) {
    Message message = builder.get().userId("prateek").build();

    analytics.enqueue(message);

    verify(interceptor).intercept(message);
    verify(client).enqueue(message);
  }

  @Test public void shutdownIsDispatched() {
    analytics.shutdown();

    verify(client).shutdown();
  }
}