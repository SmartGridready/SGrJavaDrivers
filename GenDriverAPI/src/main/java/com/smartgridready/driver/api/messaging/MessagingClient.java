package com.smartgridready.driver.api.messaging;

import com.smartgridready.ns.v0.MessageFilter;
import com.smartgridready.driver.api.common.GenDriverException;
import io.vavr.control.Either;

import java.io.Closeable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Interface to be used by smartgridready messaging client implementations.
 */
@SuppressWarnings("unused")
public interface MessagingClient extends Closeable {

    /**
     * This method publishes a message to the given topic. The method is blocking
     * until the message is acknowledged by the message broker.
     * @param topic The topic to send the message to.
     * @param message The message to send
     */
    void sendSync(String topic, Message message);

    /**
     * Subscribes to a topic and returns a completable future that provides
     * the next message from the given topic.
     * Used to receive the response message of a read-value message/command.
     *
     * @param inMessageTopic The topic to read the value from.
     * @return Either the message received or a Throwable if an error occurred.
     */
    Either<Throwable, Message> readSync(
            String readCmdMessageTopic,
            Message readCmdMessage,
            String inMessageTopic,
            MessageFilter messageFilter,
            long timeoutMs);

    /**
     * Sends a message asynchronously to a given topic. The method does not
     * block and returns immediately without checking the ACK for the
     * message to be sent. However, you could await the ACK when waiting for
     * the completable future that is returned.
     *
     * @param topic The topic to send the message to
     * @param message The message
     * @return A completable future to (optionally) wait for the broker ACK that
     *         the message has been sent.
     */
    CompletableFuture<Either<Throwable, Void>> sendAsynch(String topic, Message message);

    /**
     * Subscribes to a topic for receiving a stream of messages.
     *
     * @param topic The topic to subscribe to
     * @param callback The callback method that handles the incoming messages.
     */
    void subscribe(String topic, MessageFilter messageFilter, Consumer<Either<Throwable, Message>> callback) throws GenDriverException;

    /**
     * @param topic The topic to unsubscribe from
     */
    void unsubscribe(String topic) throws GenDriverException;
}
