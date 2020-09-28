/**
 *   _   _ _     _         ____         __ _
 *  | | | (_)___| | ____ _/ ___|  ___  / _| |_
 *  | |_| | / __| |/ / _` \___ \ / _ \| |_| __|
 *  |  _  | \__ \   < (_| |___) | (_) |  _| |_
 *  |_| |_|_|___/_|\_\__,_|____/ \___/|_|  \__|
 *
 *  Copyright © 2020 HiskaSoft
 *  http://www.hiskasoft.com/licenses/LICENSE-2.0
 */
package com.hiska.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Basic Result Response for Request
 */
@lombok.Getter
@lombok.Setter
@lombok.ToString
public class Result implements Serializable {
   /**
    * Flag attribute for error result
    */
   private boolean success = true;
   /**
    * List of Message
    */
   private List<Message> messages = new ArrayList<>();
   /**
    * List of Behavior
    */
   private List<Behavior> behaviors = new ArrayList<>();

   public Result() {
   }

   public Result(final Result other) {
      success = other.success;
      messages.addAll(other.messages);
   }

   public void addMessage(final Message message) {
      if (message != null) {
         messages.add(message);
      }
   }

   public void addAllMessage(final Collection<Message> allMessage) {
      if (allMessage != null) {
         allMessage.forEach(this::addMessage);
      }
   }

   /**
    * Clear all messages
    */
   public void clearMessage() {
      messages.clear();
   }

   public void addBehavior(final Behavior behavior) {
      if (behavior != null) {
         behaviors.add(behavior);
      }
   }

   public void addAllBehavior(final Collection<Behavior> allBehavior) {
      if (allBehavior != null) {
         allBehavior.forEach(this::addBehavior);
      }
   }

   /**
    * Clear all behaviors
    */
   public void clearBehavior() {
      behaviors.clear();
   }

   /**
    * Result Clear
    * Clear all messages
    * Clear all behaviors
    */
   public void clear() {
      messages.clear();
      behaviors.clear();
   }

   /**
    * Result Reset
    * Clear all messages
    * Clear all behaviors
    * success is true
    */
   public void reset() {
      success = true;
      messages.clear();
      behaviors.clear();
   }

   /**
    * Create a simple Result Object
    * 
    * @return
    */
   public Result asResult() {
      return new Result(this);
   }

   /**
    * Create and add a new Message
    * 
    * @return
    */
   public MessageBuilder message() {
      return MessageBuilder.create()
            .set(messages::add);
   }

   /**
    * Create and add a new Message
    * Text Message in format XXX-####: Text
    * <p>
    * XXX is a PREFIX
    * </p>
    * <p>
    * #### is a Number
    * </p>
    * <p>
    * 0### is INFO
    * </p>
    * <p>
    * 1### is INFO
    * </p>
    * <p>
    * 2### is WARN
    * </p>
    * <p>
    * 3### is ERROR
    * </p>
    * <p>
    * 9### is FATAL
    * </p>
    * 
    * @param  text
    * @return
    */
   public MessageBuilder message(String text) {
      return MessageBuilder.create(text)
            .set(messages::add);
   }

   public void ifSuccess(Runnable caller) {
      if (success) {
         caller.run();
      }
   }

   public void ifError(Runnable caller) {
      if (!success) {
         caller.run();
      }
   }

   public boolean isError() {
      return !success;
   }

   /**
    * Append Messages and Behaviors to Result
    * Message is included
    * Behavior is included
    * 
    * @param result
    */
   public void append(Result result) {
      success = result.success;
      addAllMessage(result.messages);
      addAllBehavior(result.behaviors);
   }

   /**
    * Accept Messages and Behaviors to Result
    * Message is replaced
    * Behavior is replaced
    * 
    * @param result
    */
   public void accept(Result result) {
      success = result.success;
      messages.clear();
      behaviors.clear();
      addAllMessage(result.messages);
      addAllBehavior(result.behaviors);
   }

   /**
    * Throw a new ResultException if success is false
    * 
    * @param message
    */
   public void throwException(String message) {
      if (success == false) {
         throw new ResultException(message, this);
      }
   }

   private static final Logger LOGGER = Logger.getLogger(Result.class.getName());

   /**
    * Create Log Result
    */
   public void log() {
      log("·");
   }

   /**
    * Create Log Result
    * 
    * @param title title log
    */
   public void log(String title) {
      LOGGER.log(Level.INFO, "Result:{0}|Success={1}|Messages={2}|Behaviors={3}|This={4}", new Object[]{title, success, messages, behaviors, this});
   }
}
