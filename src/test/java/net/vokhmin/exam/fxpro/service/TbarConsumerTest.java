package net.vokhmin.exam.fxpro.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

@Test
public class TbarConsumerTest {

    @BeforeClass
    void setUp() {
    }

    @AfterClass
    void tearDown() {
    }

    @Test
    public void test() throws InterruptedException {
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        publisher.subscribe(new PrintSubscriber());
        System.out.println("Submitting items...");
        for (int i = 0; i < 10; i++) {
            publisher.submit(i);
        }
        Thread.sleep(1000);
        publisher.close();
    }

    public class PrintSubscriber implements Flow.Subscriber<Integer> {
        private Flow.Subscription subscription;
        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1);
        }
        @Override
        public void onNext(Integer item) {
            System.out.println("Received item: " + item);
            subscription.request(1);
        }
        @Override
        public void onError(Throwable error) {
            System.out.println("Error occurred: " + error.getMessage());
        }
        @Override
        public void onComplete() {
            System.out.println("PrintSubscriber is complete");
        }
    }

}