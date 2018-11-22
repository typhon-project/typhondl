/**
 * generated by Xtext 2.15.0
 */
package org.typhon.dsls.xtext.typhonDL;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Comma Separated Assignment List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.typhon.dsls.xtext.typhonDL.CommaSeparatedAssignmentList#getName <em>Name</em>}</li>
 *   <li>{@link org.typhon.dsls.xtext.typhonDL.CommaSeparatedAssignmentList#getValue <em>Value</em>}</li>
 *   <li>{@link org.typhon.dsls.xtext.typhonDL.CommaSeparatedAssignmentList#getValues <em>Values</em>}</li>
 * </ul>
 *
 * @see org.typhon.dsls.xtext.typhonDL.TyphonDLPackage#getCommaSeparatedAssignmentList()
 * @model
 * @generated
 */
public interface CommaSeparatedAssignmentList extends Property
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.typhon.dsls.xtext.typhonDL.TyphonDLPackage#getCommaSeparatedAssignmentList_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.typhon.dsls.xtext.typhonDL.CommaSeparatedAssignmentList#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(String)
   * @see org.typhon.dsls.xtext.typhonDL.TyphonDLPackage#getCommaSeparatedAssignmentList_Value()
   * @model
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link org.typhon.dsls.xtext.typhonDL.CommaSeparatedAssignmentList#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

  /**
   * Returns the value of the '<em><b>Values</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Values</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Values</em>' attribute list.
   * @see org.typhon.dsls.xtext.typhonDL.TyphonDLPackage#getCommaSeparatedAssignmentList_Values()
   * @model unique="false"
   * @generated
   */
  EList<String> getValues();

} // CommaSeparatedAssignmentList
