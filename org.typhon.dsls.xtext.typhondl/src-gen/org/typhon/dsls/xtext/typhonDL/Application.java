/**
 * generated by Xtext 2.15.0
 */
package org.typhon.dsls.xtext.typhonDL;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Application</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.typhon.dsls.xtext.typhonDL.Application#getContainers <em>Containers</em>}</li>
 * </ul>
 *
 * @see org.typhon.dsls.xtext.typhonDL.TyphonDLPackage#getApplication()
 * @model
 * @generated
 */
public interface Application extends Deployment
{
  /**
   * Returns the value of the '<em><b>Containers</b></em>' containment reference list.
   * The list contents are of type {@link org.typhon.dsls.xtext.typhonDL.Container}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Containers</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Containers</em>' containment reference list.
   * @see org.typhon.dsls.xtext.typhonDL.TyphonDLPackage#getApplication_Containers()
   * @model containment="true"
   * @generated
   */
  EList<Container> getContainers();

} // Application