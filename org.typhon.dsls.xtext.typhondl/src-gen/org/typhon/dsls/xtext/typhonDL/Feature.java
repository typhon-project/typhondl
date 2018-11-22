/**
 * generated by Xtext 2.15.0
 */
package org.typhon.dsls.xtext.typhonDL;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.typhon.dsls.xtext.typhonDL.Feature#getName <em>Name</em>}</li>
 *   <li>{@link org.typhon.dsls.xtext.typhonDL.Feature#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.typhon.dsls.xtext.typhonDL.TyphonDLPackage#getFeature()
 * @model
 * @generated
 */
public interface Feature extends EObject
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
   * @see org.typhon.dsls.xtext.typhonDL.TyphonDLPackage#getFeature_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.typhon.dsls.xtext.typhonDL.Feature#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' reference.
   * @see #setType(Type)
   * @see org.typhon.dsls.xtext.typhonDL.TyphonDLPackage#getFeature_Type()
   * @model
   * @generated
   */
  Type getType();

  /**
   * Sets the value of the '{@link org.typhon.dsls.xtext.typhonDL.Feature#getType <em>Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' reference.
   * @see #getType()
   * @generated
   */
  void setType(Type value);

} // Feature
