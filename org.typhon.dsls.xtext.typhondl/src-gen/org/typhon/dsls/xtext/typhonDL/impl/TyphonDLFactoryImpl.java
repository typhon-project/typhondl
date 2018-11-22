/**
 * generated by Xtext 2.15.0
 */
package org.typhon.dsls.xtext.typhonDL.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.typhon.dsls.xtext.typhonDL.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TyphonDLFactoryImpl extends EFactoryImpl implements TyphonDLFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static TyphonDLFactory init()
  {
    try
    {
      TyphonDLFactory theTyphonDLFactory = (TyphonDLFactory)EPackage.Registry.INSTANCE.getEFactory(TyphonDLPackage.eNS_URI);
      if (theTyphonDLFactory != null)
      {
        return theTyphonDLFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new TyphonDLFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TyphonDLFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case TyphonDLPackage.DEPLOYMENT_MODEL: return createDeploymentModel();
      case TyphonDLPackage.TYPE: return createType();
      case TyphonDLPackage.DATA_TYPE: return createDataType();
      case TyphonDLPackage.PLATFORM_TYPE: return createPlatformType();
      case TyphonDLPackage.DB_TYPE: return createDBType();
      case TyphonDLPackage.DEPLOYMENT: return createDeployment();
      case TyphonDLPackage.PLATFORM: return createPlatform();
      case TyphonDLPackage.CLUSTER: return createCluster();
      case TyphonDLPackage.APPLICATION: return createApplication();
      case TyphonDLPackage.CONTAINER: return createContainer();
      case TyphonDLPackage.SERVICE: return createService();
      case TyphonDLPackage.DB_SERVICE: return createDBService();
      case TyphonDLPackage.BUSINESS_SERVICE: return createBusinessService();
      case TyphonDLPackage.ENTITY: return createEntity();
      case TyphonDLPackage.PROPERTY: return createProperty();
      case TyphonDLPackage.ENV_LIST: return createEnvList();
      case TyphonDLPackage.ASSIGNMENT_LIST: return createAssignmentList();
      case TyphonDLPackage.COMMA_SEPARATED_ASSIGNMENT_LIST: return createCommaSeparatedAssignmentList();
      case TyphonDLPackage.ASSIGNMENT: return createAssignment();
      case TyphonDLPackage.FEATURE: return createFeature();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DeploymentModel createDeploymentModel()
  {
    DeploymentModelImpl deploymentModel = new DeploymentModelImpl();
    return deploymentModel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Type createType()
  {
    TypeImpl type = new TypeImpl();
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DataType createDataType()
  {
    DataTypeImpl dataType = new DataTypeImpl();
    return dataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PlatformType createPlatformType()
  {
    PlatformTypeImpl platformType = new PlatformTypeImpl();
    return platformType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DBType createDBType()
  {
    DBTypeImpl dbType = new DBTypeImpl();
    return dbType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Deployment createDeployment()
  {
    DeploymentImpl deployment = new DeploymentImpl();
    return deployment;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Platform createPlatform()
  {
    PlatformImpl platform = new PlatformImpl();
    return platform;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Cluster createCluster()
  {
    ClusterImpl cluster = new ClusterImpl();
    return cluster;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Application createApplication()
  {
    ApplicationImpl application = new ApplicationImpl();
    return application;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public org.typhon.dsls.xtext.typhonDL.Container createContainer()
  {
    ContainerImpl container = new ContainerImpl();
    return container;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Service createService()
  {
    ServiceImpl service = new ServiceImpl();
    return service;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DBService createDBService()
  {
    DBServiceImpl dbService = new DBServiceImpl();
    return dbService;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BusinessService createBusinessService()
  {
    BusinessServiceImpl businessService = new BusinessServiceImpl();
    return businessService;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Entity createEntity()
  {
    EntityImpl entity = new EntityImpl();
    return entity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Property createProperty()
  {
    PropertyImpl property = new PropertyImpl();
    return property;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EnvList createEnvList()
  {
    EnvListImpl envList = new EnvListImpl();
    return envList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AssignmentList createAssignmentList()
  {
    AssignmentListImpl assignmentList = new AssignmentListImpl();
    return assignmentList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CommaSeparatedAssignmentList createCommaSeparatedAssignmentList()
  {
    CommaSeparatedAssignmentListImpl commaSeparatedAssignmentList = new CommaSeparatedAssignmentListImpl();
    return commaSeparatedAssignmentList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Assignment createAssignment()
  {
    AssignmentImpl assignment = new AssignmentImpl();
    return assignment;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Feature createFeature()
  {
    FeatureImpl feature = new FeatureImpl();
    return feature;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TyphonDLPackage getTyphonDLPackage()
  {
    return (TyphonDLPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static TyphonDLPackage getPackage()
  {
    return TyphonDLPackage.eINSTANCE;
  }

} //TyphonDLFactoryImpl
