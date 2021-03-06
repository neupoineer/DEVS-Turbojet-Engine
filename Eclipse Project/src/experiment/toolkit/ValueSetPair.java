/*
 * CSE 593 - Fall 2016 - Applied Project
 * Author  : Lucio Ortiz and Robert Blazewicz
 * Version : DEVSJAVA 3.0
 * Date    : 2016-11-13
 */
package experiment.toolkit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import GenCol.Pair;


/**
 * The Class ValueSetPair.
 */
public class ValueSetPair extends Pair {

  /**
   * Instantiates a new value set pair.
   *
   * @param experimentState the experiment state
   * @param valueSet the value set
   */
  public ValueSetPair(final ExperimentState experimentState, final ValueSet valueSet) {
    super(null, null);
    SerializeKey(experimentState);
    SerializeValue(valueSet);
  }

  /* (non-Javadoc)
   * @see GenCol.Pair#toString()
   */
  @Override
  public String toString() {
    final ExperimentState experimentState = DeserializeKey();
    return experimentState.getKey();
  }

  /**
   * Gets the experiment state.
   *
   * @return the experiment state
   */
  public ExperimentState getExperimentState() {
    return DeserializeKey();
  }

  /**
   * Gets the value set.
   *
   * @return the value set
   */
  public ValueSet getValueSet() {
    return DeserializeValue();
  }

  /**
   * Serialize key.
   *
   * @param experimentState the experiment state
   */
  private void SerializeKey(final ExperimentState experimentState) {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);) {
      objectOutputStream.writeObject(experimentState);
      objectOutputStream.flush();
      key = byteArrayOutputStream.toString();
    } catch (IOException e) {
      System.err.println("Failure serializing ExperimentState: " + e);
    }
  }

  /**
   * Serialize value.
   *
   * @param valueSet the value set
   */
  private void SerializeValue(final ValueSet valueSet) {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);) {
      objectOutputStream.writeObject(valueSet);
      objectOutputStream.flush();
      value = byteArrayOutputStream.toString();
    } catch (IOException e) {
      System.err.println("Failure serializing ValueSet: " + e);
    }
  }

  /**
   * Deserialize key.
   *
   * @return the experiment state
   */
  private ExperimentState DeserializeKey() {
    ExperimentState experimentState = null;
    final String tmpString = (String) key;
    final byte b[] = tmpString.getBytes();
    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);
         ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);) {
      experimentState = (ExperimentState) objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
      System.err.println("Failure deserializing ExperimentState: " + e);
    }
    return experimentState;
  }

  /**
   * Deserialize.
   *
   * @return the value set
   */
  private ValueSet DeserializeValue() {
    ValueSet valueSet = null;
    final String tmpString = (String) value;
    final byte b[] = tmpString.getBytes();
    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);
         ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);) {
      valueSet = (ValueSet) objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
      System.err.println("Failure deserializing ValueSet: " + e);
    }
    return valueSet;
  }

  /**
   * Gets the model.
   *
   * @return the model
   */
  public char getModel() {
    return getExperimentState().getModel();
  }

  /**
   * Gets the model index.
   *
   * @return the model index
   */
  public int getModelIndex() {
    return getExperimentState().getModelIndex();
  }

  /**
   * Increment pass.
   */
  public void incrementPass() {
    ExperimentState experimentState = getExperimentState();
    experimentState.incrementPass();
    SerializeKey(experimentState);
  }

  /**
   * Checks if is run terminated.
   *
   * @return true, if is run terminated
   */
  public boolean isRunTerminated() {
    return getExperimentState().isRunTerminated();
  }

  /**
   * Complete run.
   */
  public void completeRun() {
    ExperimentState experimentState = getExperimentState();
    experimentState.completeRun();
    SerializeKey(experimentState);
  }

  /**
   * Checks if is run completed.
   *
   * @return true, if is run completed
   */
  public boolean isRunCompleted() {
    return getExperimentState().isRunCompleted();
  }
}
