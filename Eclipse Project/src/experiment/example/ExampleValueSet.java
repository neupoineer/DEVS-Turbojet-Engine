/*
 * CSE 593 - Fall 2016 - Applied Project
 * Author  : Lucio Ortiz and Robert Blazewicz
 * Version : DEVSJAVA 3.0
 * Date    : 2016-10-08
 */
package experiment.example;

import experiment.toolkit.ValueSet;
import experiment.toolkit.ValueSetLoad;

/**
 * The Class ExampleValueSet.
 */
public class ExampleValueSet {

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    final ValueSet valueSet = new ValueSet();
    final ValueSetLoad valueSetLoad = new ValueSetLoad();
    System.out.println("Pathname: " + valueSetLoad.getPathname());
    System.out.println("Filename: " + valueSetLoad.getFilename());
    valueSetLoad.load(valueSet);
    System.out.println("Total: " + valueSet.getMap().size());
    valueSet.dump("> ");
  }
}
