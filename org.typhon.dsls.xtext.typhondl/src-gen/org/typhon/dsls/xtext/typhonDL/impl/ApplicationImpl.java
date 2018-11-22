/**
 * generated by Xtext 2.15.0
 */
package org.typhon.dsls.xtext.typhonDL.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.typhon.dsls.xtext.typhonDL.Application;
import org.typhon.dsls.xtext.typhonDL.TyphonDLPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Application</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.typhon.dsls.xtext.typhonDL.impl.ApplicationImpl#getContainers <em>Containers</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ApplicationImpl extends DeploymentImpl implements Application
{
  /**
   * The cached value of the '{@link #getContainers() <em>Containers</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContainers()
   * @generated
   * @ordered
   */
  protected EList<org.typhon.dsls.xtext.typhonDL.Container> containers;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ApplicationImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return TyphonDLPackage.Literals.APPLICATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<org.typhon.dsls.xtext.typhonDL.Container> getContainers()
  {
    if (containers == null)
    {
      containers = new EObjectContainmentEList<org.typhon.dsls.xtext.typhonDL.Container>(org.typhon.dsls.xtext.typhonDL.Container.class, this, TyphonDLPackage.APPLICATION__CONTAINERS);
    }
    return containers;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case TyphonDLPackage.APPLICATION__CONTAINERS:
        return ((InternalEList<?>)getContainers()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case TyphonDLPackage.APPLICATION__CONTAINERS:
        return getContainers();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case TyphonDLPackage.APPLICATION__CONTAINERS:
        getContainers().clear();
        getContainers().addAll((Collection<? extends org.typhon.dsls.xtext.typhonDL.Container>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case TyphonDLPackage.APPLICATION__CONTAINERS:
        getContainers().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case TyphonDLPackage.APPLICATION__CONTAINERS:
        return containers != null && !containers.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //ApplicationImpl
