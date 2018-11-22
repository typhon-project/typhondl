package org.typhon.dsls.xtext.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import org.typhon.dsls.xtext.services.TyphonDLGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTyphonDLParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_MYSTRING", "RULE_STRING", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'datatype'", "'platformtype'", "'dbtype'", "'platform'", "':'", "'{'", "'}'", "'cluster'", "'application'", "'container'", "'dbService'", "'businessService'", "'entity'", "'extends'", "'environment'", "'['", "','", "']'", "'='", "'/'", "'-'", "'.'"
    };
    public static final int RULE_STRING=6;
    public static final int RULE_SL_COMMENT=9;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int RULE_MYSTRING=5;
    public static final int T__33=33;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int RULE_ID=4;
    public static final int RULE_WS=10;
    public static final int RULE_ANY_OTHER=11;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=7;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int RULE_ML_COMMENT=8;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__20=20;
    public static final int T__21=21;

    // delegates
    // delegators


        public InternalTyphonDLParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalTyphonDLParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalTyphonDLParser.tokenNames; }
    public String getGrammarFileName() { return "InternalTyphonDL.g"; }



     	private TyphonDLGrammarAccess grammarAccess;

        public InternalTyphonDLParser(TokenStream input, TyphonDLGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "DeploymentModel";
       	}

       	@Override
       	protected TyphonDLGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleDeploymentModel"
    // InternalTyphonDL.g:64:1: entryRuleDeploymentModel returns [EObject current=null] : iv_ruleDeploymentModel= ruleDeploymentModel EOF ;
    public final EObject entryRuleDeploymentModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDeploymentModel = null;


        try {
            // InternalTyphonDL.g:64:56: (iv_ruleDeploymentModel= ruleDeploymentModel EOF )
            // InternalTyphonDL.g:65:2: iv_ruleDeploymentModel= ruleDeploymentModel EOF
            {
             newCompositeNode(grammarAccess.getDeploymentModelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDeploymentModel=ruleDeploymentModel();

            state._fsp--;

             current =iv_ruleDeploymentModel; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDeploymentModel"


    // $ANTLR start "ruleDeploymentModel"
    // InternalTyphonDL.g:71:1: ruleDeploymentModel returns [EObject current=null] : ( (lv_elements_0_0= ruleType ) )* ;
    public final EObject ruleDeploymentModel() throws RecognitionException {
        EObject current = null;

        EObject lv_elements_0_0 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:77:2: ( ( (lv_elements_0_0= ruleType ) )* )
            // InternalTyphonDL.g:78:2: ( (lv_elements_0_0= ruleType ) )*
            {
            // InternalTyphonDL.g:78:2: ( (lv_elements_0_0= ruleType ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=12 && LA1_0<=15)||(LA1_0>=19 && LA1_0<=24)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalTyphonDL.g:79:3: (lv_elements_0_0= ruleType )
            	    {
            	    // InternalTyphonDL.g:79:3: (lv_elements_0_0= ruleType )
            	    // InternalTyphonDL.g:80:4: lv_elements_0_0= ruleType
            	    {

            	    				newCompositeNode(grammarAccess.getDeploymentModelAccess().getElementsTypeParserRuleCall_0());
            	    			
            	    pushFollow(FOLLOW_3);
            	    lv_elements_0_0=ruleType();

            	    state._fsp--;


            	    				if (current==null) {
            	    					current = createModelElementForParent(grammarAccess.getDeploymentModelRule());
            	    				}
            	    				add(
            	    					current,
            	    					"elements",
            	    					lv_elements_0_0,
            	    					"org.typhon.dsls.xtext.TyphonDL.Type");
            	    				afterParserOrEnumRuleCall();
            	    			

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDeploymentModel"


    // $ANTLR start "entryRuleType"
    // InternalTyphonDL.g:100:1: entryRuleType returns [EObject current=null] : iv_ruleType= ruleType EOF ;
    public final EObject entryRuleType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleType = null;


        try {
            // InternalTyphonDL.g:100:45: (iv_ruleType= ruleType EOF )
            // InternalTyphonDL.g:101:2: iv_ruleType= ruleType EOF
            {
             newCompositeNode(grammarAccess.getTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleType=ruleType();

            state._fsp--;

             current =iv_ruleType; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleType"


    // $ANTLR start "ruleType"
    // InternalTyphonDL.g:107:1: ruleType returns [EObject current=null] : (this_PlatformType_0= rulePlatformType | this_DataType_1= ruleDataType | this_Deployment_2= ruleDeployment | this_DBType_3= ruleDBType | this_Entity_4= ruleEntity ) ;
    public final EObject ruleType() throws RecognitionException {
        EObject current = null;

        EObject this_PlatformType_0 = null;

        EObject this_DataType_1 = null;

        EObject this_Deployment_2 = null;

        EObject this_DBType_3 = null;

        EObject this_Entity_4 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:113:2: ( (this_PlatformType_0= rulePlatformType | this_DataType_1= ruleDataType | this_Deployment_2= ruleDeployment | this_DBType_3= ruleDBType | this_Entity_4= ruleEntity ) )
            // InternalTyphonDL.g:114:2: (this_PlatformType_0= rulePlatformType | this_DataType_1= ruleDataType | this_Deployment_2= ruleDeployment | this_DBType_3= ruleDBType | this_Entity_4= ruleEntity )
            {
            // InternalTyphonDL.g:114:2: (this_PlatformType_0= rulePlatformType | this_DataType_1= ruleDataType | this_Deployment_2= ruleDeployment | this_DBType_3= ruleDBType | this_Entity_4= ruleEntity )
            int alt2=5;
            switch ( input.LA(1) ) {
            case 13:
                {
                alt2=1;
                }
                break;
            case 12:
                {
                alt2=2;
                }
                break;
            case 15:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                {
                alt2=3;
                }
                break;
            case 14:
                {
                alt2=4;
                }
                break;
            case 24:
                {
                alt2=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // InternalTyphonDL.g:115:3: this_PlatformType_0= rulePlatformType
                    {

                    			newCompositeNode(grammarAccess.getTypeAccess().getPlatformTypeParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_PlatformType_0=rulePlatformType();

                    state._fsp--;


                    			current = this_PlatformType_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalTyphonDL.g:124:3: this_DataType_1= ruleDataType
                    {

                    			newCompositeNode(grammarAccess.getTypeAccess().getDataTypeParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_DataType_1=ruleDataType();

                    state._fsp--;


                    			current = this_DataType_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalTyphonDL.g:133:3: this_Deployment_2= ruleDeployment
                    {

                    			newCompositeNode(grammarAccess.getTypeAccess().getDeploymentParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_Deployment_2=ruleDeployment();

                    state._fsp--;


                    			current = this_Deployment_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalTyphonDL.g:142:3: this_DBType_3= ruleDBType
                    {

                    			newCompositeNode(grammarAccess.getTypeAccess().getDBTypeParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_DBType_3=ruleDBType();

                    state._fsp--;


                    			current = this_DBType_3;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 5 :
                    // InternalTyphonDL.g:151:3: this_Entity_4= ruleEntity
                    {

                    			newCompositeNode(grammarAccess.getTypeAccess().getEntityParserRuleCall_4());
                    		
                    pushFollow(FOLLOW_2);
                    this_Entity_4=ruleEntity();

                    state._fsp--;


                    			current = this_Entity_4;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleType"


    // $ANTLR start "entryRuleDataType"
    // InternalTyphonDL.g:163:1: entryRuleDataType returns [EObject current=null] : iv_ruleDataType= ruleDataType EOF ;
    public final EObject entryRuleDataType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDataType = null;


        try {
            // InternalTyphonDL.g:163:49: (iv_ruleDataType= ruleDataType EOF )
            // InternalTyphonDL.g:164:2: iv_ruleDataType= ruleDataType EOF
            {
             newCompositeNode(grammarAccess.getDataTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDataType=ruleDataType();

            state._fsp--;

             current =iv_ruleDataType; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDataType"


    // $ANTLR start "ruleDataType"
    // InternalTyphonDL.g:170:1: ruleDataType returns [EObject current=null] : (otherlv_0= 'datatype' ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleDataType() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;


        	enterRule();

        try {
            // InternalTyphonDL.g:176:2: ( (otherlv_0= 'datatype' ( (lv_name_1_0= RULE_ID ) ) ) )
            // InternalTyphonDL.g:177:2: (otherlv_0= 'datatype' ( (lv_name_1_0= RULE_ID ) ) )
            {
            // InternalTyphonDL.g:177:2: (otherlv_0= 'datatype' ( (lv_name_1_0= RULE_ID ) ) )
            // InternalTyphonDL.g:178:3: otherlv_0= 'datatype' ( (lv_name_1_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,12,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getDataTypeAccess().getDatatypeKeyword_0());
            		
            // InternalTyphonDL.g:182:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalTyphonDL.g:183:4: (lv_name_1_0= RULE_ID )
            {
            // InternalTyphonDL.g:183:4: (lv_name_1_0= RULE_ID )
            // InternalTyphonDL.g:184:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(lv_name_1_0, grammarAccess.getDataTypeAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDataTypeRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDataType"


    // $ANTLR start "entryRulePlatformType"
    // InternalTyphonDL.g:204:1: entryRulePlatformType returns [EObject current=null] : iv_rulePlatformType= rulePlatformType EOF ;
    public final EObject entryRulePlatformType() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlatformType = null;


        try {
            // InternalTyphonDL.g:204:53: (iv_rulePlatformType= rulePlatformType EOF )
            // InternalTyphonDL.g:205:2: iv_rulePlatformType= rulePlatformType EOF
            {
             newCompositeNode(grammarAccess.getPlatformTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePlatformType=rulePlatformType();

            state._fsp--;

             current =iv_rulePlatformType; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePlatformType"


    // $ANTLR start "rulePlatformType"
    // InternalTyphonDL.g:211:1: rulePlatformType returns [EObject current=null] : (otherlv_0= 'platformtype' ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject rulePlatformType() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;


        	enterRule();

        try {
            // InternalTyphonDL.g:217:2: ( (otherlv_0= 'platformtype' ( (lv_name_1_0= RULE_ID ) ) ) )
            // InternalTyphonDL.g:218:2: (otherlv_0= 'platformtype' ( (lv_name_1_0= RULE_ID ) ) )
            {
            // InternalTyphonDL.g:218:2: (otherlv_0= 'platformtype' ( (lv_name_1_0= RULE_ID ) ) )
            // InternalTyphonDL.g:219:3: otherlv_0= 'platformtype' ( (lv_name_1_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,13,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getPlatformTypeAccess().getPlatformtypeKeyword_0());
            		
            // InternalTyphonDL.g:223:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalTyphonDL.g:224:4: (lv_name_1_0= RULE_ID )
            {
            // InternalTyphonDL.g:224:4: (lv_name_1_0= RULE_ID )
            // InternalTyphonDL.g:225:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(lv_name_1_0, grammarAccess.getPlatformTypeAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getPlatformTypeRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePlatformType"


    // $ANTLR start "entryRuleDBType"
    // InternalTyphonDL.g:245:1: entryRuleDBType returns [EObject current=null] : iv_ruleDBType= ruleDBType EOF ;
    public final EObject entryRuleDBType() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDBType = null;


        try {
            // InternalTyphonDL.g:245:47: (iv_ruleDBType= ruleDBType EOF )
            // InternalTyphonDL.g:246:2: iv_ruleDBType= ruleDBType EOF
            {
             newCompositeNode(grammarAccess.getDBTypeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDBType=ruleDBType();

            state._fsp--;

             current =iv_ruleDBType; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDBType"


    // $ANTLR start "ruleDBType"
    // InternalTyphonDL.g:252:1: ruleDBType returns [EObject current=null] : (otherlv_0= 'dbtype' ( (lv_name_1_0= RULE_ID ) ) ) ;
    public final EObject ruleDBType() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;


        	enterRule();

        try {
            // InternalTyphonDL.g:258:2: ( (otherlv_0= 'dbtype' ( (lv_name_1_0= RULE_ID ) ) ) )
            // InternalTyphonDL.g:259:2: (otherlv_0= 'dbtype' ( (lv_name_1_0= RULE_ID ) ) )
            {
            // InternalTyphonDL.g:259:2: (otherlv_0= 'dbtype' ( (lv_name_1_0= RULE_ID ) ) )
            // InternalTyphonDL.g:260:3: otherlv_0= 'dbtype' ( (lv_name_1_0= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,14,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getDBTypeAccess().getDbtypeKeyword_0());
            		
            // InternalTyphonDL.g:264:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalTyphonDL.g:265:4: (lv_name_1_0= RULE_ID )
            {
            // InternalTyphonDL.g:265:4: (lv_name_1_0= RULE_ID )
            // InternalTyphonDL.g:266:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(lv_name_1_0, grammarAccess.getDBTypeAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDBTypeRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDBType"


    // $ANTLR start "entryRuleDeployment"
    // InternalTyphonDL.g:286:1: entryRuleDeployment returns [EObject current=null] : iv_ruleDeployment= ruleDeployment EOF ;
    public final EObject entryRuleDeployment() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDeployment = null;


        try {
            // InternalTyphonDL.g:286:51: (iv_ruleDeployment= ruleDeployment EOF )
            // InternalTyphonDL.g:287:2: iv_ruleDeployment= ruleDeployment EOF
            {
             newCompositeNode(grammarAccess.getDeploymentRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDeployment=ruleDeployment();

            state._fsp--;

             current =iv_ruleDeployment; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDeployment"


    // $ANTLR start "ruleDeployment"
    // InternalTyphonDL.g:293:1: ruleDeployment returns [EObject current=null] : (this_Platform_0= rulePlatform | this_Cluster_1= ruleCluster | this_Application_2= ruleApplication | this_Container_3= ruleContainer | this_Service_4= ruleService ) ;
    public final EObject ruleDeployment() throws RecognitionException {
        EObject current = null;

        EObject this_Platform_0 = null;

        EObject this_Cluster_1 = null;

        EObject this_Application_2 = null;

        EObject this_Container_3 = null;

        EObject this_Service_4 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:299:2: ( (this_Platform_0= rulePlatform | this_Cluster_1= ruleCluster | this_Application_2= ruleApplication | this_Container_3= ruleContainer | this_Service_4= ruleService ) )
            // InternalTyphonDL.g:300:2: (this_Platform_0= rulePlatform | this_Cluster_1= ruleCluster | this_Application_2= ruleApplication | this_Container_3= ruleContainer | this_Service_4= ruleService )
            {
            // InternalTyphonDL.g:300:2: (this_Platform_0= rulePlatform | this_Cluster_1= ruleCluster | this_Application_2= ruleApplication | this_Container_3= ruleContainer | this_Service_4= ruleService )
            int alt3=5;
            switch ( input.LA(1) ) {
            case 15:
                {
                alt3=1;
                }
                break;
            case 19:
                {
                alt3=2;
                }
                break;
            case 20:
                {
                alt3=3;
                }
                break;
            case 21:
                {
                alt3=4;
                }
                break;
            case 22:
            case 23:
                {
                alt3=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // InternalTyphonDL.g:301:3: this_Platform_0= rulePlatform
                    {

                    			newCompositeNode(grammarAccess.getDeploymentAccess().getPlatformParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_Platform_0=rulePlatform();

                    state._fsp--;


                    			current = this_Platform_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalTyphonDL.g:310:3: this_Cluster_1= ruleCluster
                    {

                    			newCompositeNode(grammarAccess.getDeploymentAccess().getClusterParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_Cluster_1=ruleCluster();

                    state._fsp--;


                    			current = this_Cluster_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalTyphonDL.g:319:3: this_Application_2= ruleApplication
                    {

                    			newCompositeNode(grammarAccess.getDeploymentAccess().getApplicationParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_Application_2=ruleApplication();

                    state._fsp--;


                    			current = this_Application_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalTyphonDL.g:328:3: this_Container_3= ruleContainer
                    {

                    			newCompositeNode(grammarAccess.getDeploymentAccess().getContainerParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_Container_3=ruleContainer();

                    state._fsp--;


                    			current = this_Container_3;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 5 :
                    // InternalTyphonDL.g:337:3: this_Service_4= ruleService
                    {

                    			newCompositeNode(grammarAccess.getDeploymentAccess().getServiceParserRuleCall_4());
                    		
                    pushFollow(FOLLOW_2);
                    this_Service_4=ruleService();

                    state._fsp--;


                    			current = this_Service_4;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDeployment"


    // $ANTLR start "entryRulePlatform"
    // InternalTyphonDL.g:349:1: entryRulePlatform returns [EObject current=null] : iv_rulePlatform= rulePlatform EOF ;
    public final EObject entryRulePlatform() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlatform = null;


        try {
            // InternalTyphonDL.g:349:49: (iv_rulePlatform= rulePlatform EOF )
            // InternalTyphonDL.g:350:2: iv_rulePlatform= rulePlatform EOF
            {
             newCompositeNode(grammarAccess.getPlatformRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePlatform=rulePlatform();

            state._fsp--;

             current =iv_rulePlatform; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePlatform"


    // $ANTLR start "rulePlatform"
    // InternalTyphonDL.g:356:1: rulePlatform returns [EObject current=null] : ( () otherlv_1= 'platform' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (otherlv_4= RULE_ID ) ) otherlv_5= '{' ( (lv_clusters_6_0= ruleCluster ) )* otherlv_7= '}' ) ;
    public final EObject rulePlatform() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token otherlv_7=null;
        EObject lv_clusters_6_0 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:362:2: ( ( () otherlv_1= 'platform' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (otherlv_4= RULE_ID ) ) otherlv_5= '{' ( (lv_clusters_6_0= ruleCluster ) )* otherlv_7= '}' ) )
            // InternalTyphonDL.g:363:2: ( () otherlv_1= 'platform' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (otherlv_4= RULE_ID ) ) otherlv_5= '{' ( (lv_clusters_6_0= ruleCluster ) )* otherlv_7= '}' )
            {
            // InternalTyphonDL.g:363:2: ( () otherlv_1= 'platform' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (otherlv_4= RULE_ID ) ) otherlv_5= '{' ( (lv_clusters_6_0= ruleCluster ) )* otherlv_7= '}' )
            // InternalTyphonDL.g:364:3: () otherlv_1= 'platform' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= ':' ( (otherlv_4= RULE_ID ) ) otherlv_5= '{' ( (lv_clusters_6_0= ruleCluster ) )* otherlv_7= '}'
            {
            // InternalTyphonDL.g:364:3: ()
            // InternalTyphonDL.g:365:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getPlatformAccess().getPlatformAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,15,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getPlatformAccess().getPlatformKeyword_1());
            		
            // InternalTyphonDL.g:375:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalTyphonDL.g:376:4: (lv_name_2_0= RULE_ID )
            {
            // InternalTyphonDL.g:376:4: (lv_name_2_0= RULE_ID )
            // InternalTyphonDL.g:377:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            					newLeafNode(lv_name_2_0, grammarAccess.getPlatformAccess().getNameIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getPlatformRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_2_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_3=(Token)match(input,16,FOLLOW_4); 

            			newLeafNode(otherlv_3, grammarAccess.getPlatformAccess().getColonKeyword_3());
            		
            // InternalTyphonDL.g:397:3: ( (otherlv_4= RULE_ID ) )
            // InternalTyphonDL.g:398:4: (otherlv_4= RULE_ID )
            {
            // InternalTyphonDL.g:398:4: (otherlv_4= RULE_ID )
            // InternalTyphonDL.g:399:5: otherlv_4= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getPlatformRule());
            					}
            				
            otherlv_4=(Token)match(input,RULE_ID,FOLLOW_6); 

            					newLeafNode(otherlv_4, grammarAccess.getPlatformAccess().getTypePlatformTypeCrossReference_4_0());
            				

            }


            }

            otherlv_5=(Token)match(input,17,FOLLOW_7); 

            			newLeafNode(otherlv_5, grammarAccess.getPlatformAccess().getLeftCurlyBracketKeyword_5());
            		
            // InternalTyphonDL.g:414:3: ( (lv_clusters_6_0= ruleCluster ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==19) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalTyphonDL.g:415:4: (lv_clusters_6_0= ruleCluster )
            	    {
            	    // InternalTyphonDL.g:415:4: (lv_clusters_6_0= ruleCluster )
            	    // InternalTyphonDL.g:416:5: lv_clusters_6_0= ruleCluster
            	    {

            	    					newCompositeNode(grammarAccess.getPlatformAccess().getClustersClusterParserRuleCall_6_0());
            	    				
            	    pushFollow(FOLLOW_7);
            	    lv_clusters_6_0=ruleCluster();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getPlatformRule());
            	    					}
            	    					add(
            	    						current,
            	    						"clusters",
            	    						lv_clusters_6_0,
            	    						"org.typhon.dsls.xtext.TyphonDL.Cluster");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            otherlv_7=(Token)match(input,18,FOLLOW_2); 

            			newLeafNode(otherlv_7, grammarAccess.getPlatformAccess().getRightCurlyBracketKeyword_7());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePlatform"


    // $ANTLR start "entryRuleCluster"
    // InternalTyphonDL.g:441:1: entryRuleCluster returns [EObject current=null] : iv_ruleCluster= ruleCluster EOF ;
    public final EObject entryRuleCluster() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCluster = null;


        try {
            // InternalTyphonDL.g:441:48: (iv_ruleCluster= ruleCluster EOF )
            // InternalTyphonDL.g:442:2: iv_ruleCluster= ruleCluster EOF
            {
             newCompositeNode(grammarAccess.getClusterRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCluster=ruleCluster();

            state._fsp--;

             current =iv_ruleCluster; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCluster"


    // $ANTLR start "ruleCluster"
    // InternalTyphonDL.g:448:1: ruleCluster returns [EObject current=null] : ( () otherlv_1= 'cluster' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_applications_4_0= ruleApplication ) )* otherlv_5= '}' ) ;
    public final EObject ruleCluster() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_applications_4_0 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:454:2: ( ( () otherlv_1= 'cluster' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_applications_4_0= ruleApplication ) )* otherlv_5= '}' ) )
            // InternalTyphonDL.g:455:2: ( () otherlv_1= 'cluster' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_applications_4_0= ruleApplication ) )* otherlv_5= '}' )
            {
            // InternalTyphonDL.g:455:2: ( () otherlv_1= 'cluster' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_applications_4_0= ruleApplication ) )* otherlv_5= '}' )
            // InternalTyphonDL.g:456:3: () otherlv_1= 'cluster' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_applications_4_0= ruleApplication ) )* otherlv_5= '}'
            {
            // InternalTyphonDL.g:456:3: ()
            // InternalTyphonDL.g:457:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getClusterAccess().getClusterAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,19,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getClusterAccess().getClusterKeyword_1());
            		
            // InternalTyphonDL.g:467:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalTyphonDL.g:468:4: (lv_name_2_0= RULE_ID )
            {
            // InternalTyphonDL.g:468:4: (lv_name_2_0= RULE_ID )
            // InternalTyphonDL.g:469:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_6); 

            					newLeafNode(lv_name_2_0, grammarAccess.getClusterAccess().getNameIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getClusterRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_2_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_3=(Token)match(input,17,FOLLOW_8); 

            			newLeafNode(otherlv_3, grammarAccess.getClusterAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalTyphonDL.g:489:3: ( (lv_applications_4_0= ruleApplication ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==20) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalTyphonDL.g:490:4: (lv_applications_4_0= ruleApplication )
            	    {
            	    // InternalTyphonDL.g:490:4: (lv_applications_4_0= ruleApplication )
            	    // InternalTyphonDL.g:491:5: lv_applications_4_0= ruleApplication
            	    {

            	    					newCompositeNode(grammarAccess.getClusterAccess().getApplicationsApplicationParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_8);
            	    lv_applications_4_0=ruleApplication();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getClusterRule());
            	    					}
            	    					add(
            	    						current,
            	    						"applications",
            	    						lv_applications_4_0,
            	    						"org.typhon.dsls.xtext.TyphonDL.Application");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            otherlv_5=(Token)match(input,18,FOLLOW_2); 

            			newLeafNode(otherlv_5, grammarAccess.getClusterAccess().getRightCurlyBracketKeyword_5());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCluster"


    // $ANTLR start "entryRuleApplication"
    // InternalTyphonDL.g:516:1: entryRuleApplication returns [EObject current=null] : iv_ruleApplication= ruleApplication EOF ;
    public final EObject entryRuleApplication() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleApplication = null;


        try {
            // InternalTyphonDL.g:516:52: (iv_ruleApplication= ruleApplication EOF )
            // InternalTyphonDL.g:517:2: iv_ruleApplication= ruleApplication EOF
            {
             newCompositeNode(grammarAccess.getApplicationRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleApplication=ruleApplication();

            state._fsp--;

             current =iv_ruleApplication; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleApplication"


    // $ANTLR start "ruleApplication"
    // InternalTyphonDL.g:523:1: ruleApplication returns [EObject current=null] : ( () otherlv_1= 'application' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_containers_4_0= ruleContainer ) )* otherlv_5= '}' ) ;
    public final EObject ruleApplication() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_containers_4_0 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:529:2: ( ( () otherlv_1= 'application' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_containers_4_0= ruleContainer ) )* otherlv_5= '}' ) )
            // InternalTyphonDL.g:530:2: ( () otherlv_1= 'application' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_containers_4_0= ruleContainer ) )* otherlv_5= '}' )
            {
            // InternalTyphonDL.g:530:2: ( () otherlv_1= 'application' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_containers_4_0= ruleContainer ) )* otherlv_5= '}' )
            // InternalTyphonDL.g:531:3: () otherlv_1= 'application' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( (lv_containers_4_0= ruleContainer ) )* otherlv_5= '}'
            {
            // InternalTyphonDL.g:531:3: ()
            // InternalTyphonDL.g:532:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getApplicationAccess().getApplicationAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,20,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getApplicationAccess().getApplicationKeyword_1());
            		
            // InternalTyphonDL.g:542:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalTyphonDL.g:543:4: (lv_name_2_0= RULE_ID )
            {
            // InternalTyphonDL.g:543:4: (lv_name_2_0= RULE_ID )
            // InternalTyphonDL.g:544:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_6); 

            					newLeafNode(lv_name_2_0, grammarAccess.getApplicationAccess().getNameIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getApplicationRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_2_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_3=(Token)match(input,17,FOLLOW_9); 

            			newLeafNode(otherlv_3, grammarAccess.getApplicationAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalTyphonDL.g:564:3: ( (lv_containers_4_0= ruleContainer ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==21) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // InternalTyphonDL.g:565:4: (lv_containers_4_0= ruleContainer )
            	    {
            	    // InternalTyphonDL.g:565:4: (lv_containers_4_0= ruleContainer )
            	    // InternalTyphonDL.g:566:5: lv_containers_4_0= ruleContainer
            	    {

            	    					newCompositeNode(grammarAccess.getApplicationAccess().getContainersContainerParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_9);
            	    lv_containers_4_0=ruleContainer();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getApplicationRule());
            	    					}
            	    					add(
            	    						current,
            	    						"containers",
            	    						lv_containers_4_0,
            	    						"org.typhon.dsls.xtext.TyphonDL.Container");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            otherlv_5=(Token)match(input,18,FOLLOW_2); 

            			newLeafNode(otherlv_5, grammarAccess.getApplicationAccess().getRightCurlyBracketKeyword_5());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleApplication"


    // $ANTLR start "entryRuleContainer"
    // InternalTyphonDL.g:591:1: entryRuleContainer returns [EObject current=null] : iv_ruleContainer= ruleContainer EOF ;
    public final EObject entryRuleContainer() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleContainer = null;


        try {
            // InternalTyphonDL.g:591:50: (iv_ruleContainer= ruleContainer EOF )
            // InternalTyphonDL.g:592:2: iv_ruleContainer= ruleContainer EOF
            {
             newCompositeNode(grammarAccess.getContainerRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleContainer=ruleContainer();

            state._fsp--;

             current =iv_ruleContainer; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleContainer"


    // $ANTLR start "ruleContainer"
    // InternalTyphonDL.g:598:1: ruleContainer returns [EObject current=null] : ( () otherlv_1= 'container' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( ( (lv_properties_4_0= ruleProperty ) ) | ( (lv_features_5_0= ruleFeature ) ) )* otherlv_6= '}' ) ;
    public final EObject ruleContainer() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_6=null;
        EObject lv_properties_4_0 = null;

        EObject lv_features_5_0 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:604:2: ( ( () otherlv_1= 'container' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( ( (lv_properties_4_0= ruleProperty ) ) | ( (lv_features_5_0= ruleFeature ) ) )* otherlv_6= '}' ) )
            // InternalTyphonDL.g:605:2: ( () otherlv_1= 'container' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( ( (lv_properties_4_0= ruleProperty ) ) | ( (lv_features_5_0= ruleFeature ) ) )* otherlv_6= '}' )
            {
            // InternalTyphonDL.g:605:2: ( () otherlv_1= 'container' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( ( (lv_properties_4_0= ruleProperty ) ) | ( (lv_features_5_0= ruleFeature ) ) )* otherlv_6= '}' )
            // InternalTyphonDL.g:606:3: () otherlv_1= 'container' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= '{' ( ( (lv_properties_4_0= ruleProperty ) ) | ( (lv_features_5_0= ruleFeature ) ) )* otherlv_6= '}'
            {
            // InternalTyphonDL.g:606:3: ()
            // InternalTyphonDL.g:607:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getContainerAccess().getContainerAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,21,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getContainerAccess().getContainerKeyword_1());
            		
            // InternalTyphonDL.g:617:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalTyphonDL.g:618:4: (lv_name_2_0= RULE_ID )
            {
            // InternalTyphonDL.g:618:4: (lv_name_2_0= RULE_ID )
            // InternalTyphonDL.g:619:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_6); 

            					newLeafNode(lv_name_2_0, grammarAccess.getContainerAccess().getNameIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getContainerRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_2_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_3=(Token)match(input,17,FOLLOW_10); 

            			newLeafNode(otherlv_3, grammarAccess.getContainerAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalTyphonDL.g:639:3: ( ( (lv_properties_4_0= ruleProperty ) ) | ( (lv_features_5_0= ruleFeature ) ) )*
            loop7:
            do {
                int alt7=3;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==RULE_ID) ) {
                    int LA7_2 = input.LA(2);

                    if ( (LA7_2==17||LA7_2==27||LA7_2==30) ) {
                        alt7=1;
                    }
                    else if ( (LA7_2==16) ) {
                        alt7=2;
                    }


                }
                else if ( (LA7_0==26) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalTyphonDL.g:640:4: ( (lv_properties_4_0= ruleProperty ) )
            	    {
            	    // InternalTyphonDL.g:640:4: ( (lv_properties_4_0= ruleProperty ) )
            	    // InternalTyphonDL.g:641:5: (lv_properties_4_0= ruleProperty )
            	    {
            	    // InternalTyphonDL.g:641:5: (lv_properties_4_0= ruleProperty )
            	    // InternalTyphonDL.g:642:6: lv_properties_4_0= ruleProperty
            	    {

            	    						newCompositeNode(grammarAccess.getContainerAccess().getPropertiesPropertyParserRuleCall_4_0_0());
            	    					
            	    pushFollow(FOLLOW_10);
            	    lv_properties_4_0=ruleProperty();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getContainerRule());
            	    						}
            	    						add(
            	    							current,
            	    							"properties",
            	    							lv_properties_4_0,
            	    							"org.typhon.dsls.xtext.TyphonDL.Property");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;
            	case 2 :
            	    // InternalTyphonDL.g:660:4: ( (lv_features_5_0= ruleFeature ) )
            	    {
            	    // InternalTyphonDL.g:660:4: ( (lv_features_5_0= ruleFeature ) )
            	    // InternalTyphonDL.g:661:5: (lv_features_5_0= ruleFeature )
            	    {
            	    // InternalTyphonDL.g:661:5: (lv_features_5_0= ruleFeature )
            	    // InternalTyphonDL.g:662:6: lv_features_5_0= ruleFeature
            	    {

            	    						newCompositeNode(grammarAccess.getContainerAccess().getFeaturesFeatureParserRuleCall_4_1_0());
            	    					
            	    pushFollow(FOLLOW_10);
            	    lv_features_5_0=ruleFeature();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getContainerRule());
            	    						}
            	    						add(
            	    							current,
            	    							"features",
            	    							lv_features_5_0,
            	    							"org.typhon.dsls.xtext.TyphonDL.Feature");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            otherlv_6=(Token)match(input,18,FOLLOW_2); 

            			newLeafNode(otherlv_6, grammarAccess.getContainerAccess().getRightCurlyBracketKeyword_5());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleContainer"


    // $ANTLR start "entryRuleService"
    // InternalTyphonDL.g:688:1: entryRuleService returns [EObject current=null] : iv_ruleService= ruleService EOF ;
    public final EObject entryRuleService() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleService = null;


        try {
            // InternalTyphonDL.g:688:48: (iv_ruleService= ruleService EOF )
            // InternalTyphonDL.g:689:2: iv_ruleService= ruleService EOF
            {
             newCompositeNode(grammarAccess.getServiceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleService=ruleService();

            state._fsp--;

             current =iv_ruleService; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleService"


    // $ANTLR start "ruleService"
    // InternalTyphonDL.g:695:1: ruleService returns [EObject current=null] : (this_DBService_0= ruleDBService | this_BusinessService_1= ruleBusinessService ) ;
    public final EObject ruleService() throws RecognitionException {
        EObject current = null;

        EObject this_DBService_0 = null;

        EObject this_BusinessService_1 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:701:2: ( (this_DBService_0= ruleDBService | this_BusinessService_1= ruleBusinessService ) )
            // InternalTyphonDL.g:702:2: (this_DBService_0= ruleDBService | this_BusinessService_1= ruleBusinessService )
            {
            // InternalTyphonDL.g:702:2: (this_DBService_0= ruleDBService | this_BusinessService_1= ruleBusinessService )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==22) ) {
                alt8=1;
            }
            else if ( (LA8_0==23) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // InternalTyphonDL.g:703:3: this_DBService_0= ruleDBService
                    {

                    			newCompositeNode(grammarAccess.getServiceAccess().getDBServiceParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_DBService_0=ruleDBService();

                    state._fsp--;


                    			current = this_DBService_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalTyphonDL.g:712:3: this_BusinessService_1= ruleBusinessService
                    {

                    			newCompositeNode(grammarAccess.getServiceAccess().getBusinessServiceParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_BusinessService_1=ruleBusinessService();

                    state._fsp--;


                    			current = this_BusinessService_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleService"


    // $ANTLR start "entryRuleDBService"
    // InternalTyphonDL.g:724:1: entryRuleDBService returns [EObject current=null] : iv_ruleDBService= ruleDBService EOF ;
    public final EObject entryRuleDBService() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDBService = null;


        try {
            // InternalTyphonDL.g:724:50: (iv_ruleDBService= ruleDBService EOF )
            // InternalTyphonDL.g:725:2: iv_ruleDBService= ruleDBService EOF
            {
             newCompositeNode(grammarAccess.getDBServiceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDBService=ruleDBService();

            state._fsp--;

             current =iv_ruleDBService; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDBService"


    // $ANTLR start "ruleDBService"
    // InternalTyphonDL.g:731:1: ruleDBService returns [EObject current=null] : (otherlv_0= 'dbService' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_features_3_0= ruleFeature ) )* otherlv_4= '}' ) ;
    public final EObject ruleDBService() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_features_3_0 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:737:2: ( (otherlv_0= 'dbService' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_features_3_0= ruleFeature ) )* otherlv_4= '}' ) )
            // InternalTyphonDL.g:738:2: (otherlv_0= 'dbService' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_features_3_0= ruleFeature ) )* otherlv_4= '}' )
            {
            // InternalTyphonDL.g:738:2: (otherlv_0= 'dbService' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_features_3_0= ruleFeature ) )* otherlv_4= '}' )
            // InternalTyphonDL.g:739:3: otherlv_0= 'dbService' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_features_3_0= ruleFeature ) )* otherlv_4= '}'
            {
            otherlv_0=(Token)match(input,22,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getDBServiceAccess().getDbServiceKeyword_0());
            		
            // InternalTyphonDL.g:743:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalTyphonDL.g:744:4: (lv_name_1_0= RULE_ID )
            {
            // InternalTyphonDL.g:744:4: (lv_name_1_0= RULE_ID )
            // InternalTyphonDL.g:745:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_6); 

            					newLeafNode(lv_name_1_0, grammarAccess.getDBServiceAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDBServiceRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,17,FOLLOW_10); 

            			newLeafNode(otherlv_2, grammarAccess.getDBServiceAccess().getLeftCurlyBracketKeyword_2());
            		
            // InternalTyphonDL.g:765:3: ( (lv_features_3_0= ruleFeature ) )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==RULE_ID) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalTyphonDL.g:766:4: (lv_features_3_0= ruleFeature )
            	    {
            	    // InternalTyphonDL.g:766:4: (lv_features_3_0= ruleFeature )
            	    // InternalTyphonDL.g:767:5: lv_features_3_0= ruleFeature
            	    {

            	    					newCompositeNode(grammarAccess.getDBServiceAccess().getFeaturesFeatureParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_10);
            	    lv_features_3_0=ruleFeature();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getDBServiceRule());
            	    					}
            	    					add(
            	    						current,
            	    						"features",
            	    						lv_features_3_0,
            	    						"org.typhon.dsls.xtext.TyphonDL.Feature");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            otherlv_4=(Token)match(input,18,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getDBServiceAccess().getRightCurlyBracketKeyword_4());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDBService"


    // $ANTLR start "entryRuleBusinessService"
    // InternalTyphonDL.g:792:1: entryRuleBusinessService returns [EObject current=null] : iv_ruleBusinessService= ruleBusinessService EOF ;
    public final EObject entryRuleBusinessService() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBusinessService = null;


        try {
            // InternalTyphonDL.g:792:56: (iv_ruleBusinessService= ruleBusinessService EOF )
            // InternalTyphonDL.g:793:2: iv_ruleBusinessService= ruleBusinessService EOF
            {
             newCompositeNode(grammarAccess.getBusinessServiceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleBusinessService=ruleBusinessService();

            state._fsp--;

             current =iv_ruleBusinessService; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleBusinessService"


    // $ANTLR start "ruleBusinessService"
    // InternalTyphonDL.g:799:1: ruleBusinessService returns [EObject current=null] : (otherlv_0= 'businessService' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_features_3_0= ruleFeature ) )* otherlv_4= '}' ) ;
    public final EObject ruleBusinessService() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_features_3_0 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:805:2: ( (otherlv_0= 'businessService' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_features_3_0= ruleFeature ) )* otherlv_4= '}' ) )
            // InternalTyphonDL.g:806:2: (otherlv_0= 'businessService' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_features_3_0= ruleFeature ) )* otherlv_4= '}' )
            {
            // InternalTyphonDL.g:806:2: (otherlv_0= 'businessService' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_features_3_0= ruleFeature ) )* otherlv_4= '}' )
            // InternalTyphonDL.g:807:3: otherlv_0= 'businessService' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= '{' ( (lv_features_3_0= ruleFeature ) )* otherlv_4= '}'
            {
            otherlv_0=(Token)match(input,23,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getBusinessServiceAccess().getBusinessServiceKeyword_0());
            		
            // InternalTyphonDL.g:811:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalTyphonDL.g:812:4: (lv_name_1_0= RULE_ID )
            {
            // InternalTyphonDL.g:812:4: (lv_name_1_0= RULE_ID )
            // InternalTyphonDL.g:813:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_6); 

            					newLeafNode(lv_name_1_0, grammarAccess.getBusinessServiceAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getBusinessServiceRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,17,FOLLOW_10); 

            			newLeafNode(otherlv_2, grammarAccess.getBusinessServiceAccess().getLeftCurlyBracketKeyword_2());
            		
            // InternalTyphonDL.g:833:3: ( (lv_features_3_0= ruleFeature ) )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==RULE_ID) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // InternalTyphonDL.g:834:4: (lv_features_3_0= ruleFeature )
            	    {
            	    // InternalTyphonDL.g:834:4: (lv_features_3_0= ruleFeature )
            	    // InternalTyphonDL.g:835:5: lv_features_3_0= ruleFeature
            	    {

            	    					newCompositeNode(grammarAccess.getBusinessServiceAccess().getFeaturesFeatureParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_10);
            	    lv_features_3_0=ruleFeature();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getBusinessServiceRule());
            	    					}
            	    					add(
            	    						current,
            	    						"features",
            	    						lv_features_3_0,
            	    						"org.typhon.dsls.xtext.TyphonDL.Feature");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            otherlv_4=(Token)match(input,18,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getBusinessServiceAccess().getRightCurlyBracketKeyword_4());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleBusinessService"


    // $ANTLR start "entryRuleEntity"
    // InternalTyphonDL.g:860:1: entryRuleEntity returns [EObject current=null] : iv_ruleEntity= ruleEntity EOF ;
    public final EObject entryRuleEntity() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEntity = null;


        try {
            // InternalTyphonDL.g:860:47: (iv_ruleEntity= ruleEntity EOF )
            // InternalTyphonDL.g:861:2: iv_ruleEntity= ruleEntity EOF
            {
             newCompositeNode(grammarAccess.getEntityRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEntity=ruleEntity();

            state._fsp--;

             current =iv_ruleEntity; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEntity"


    // $ANTLR start "ruleEntity"
    // InternalTyphonDL.g:867:1: ruleEntity returns [EObject current=null] : (otherlv_0= 'entity' ( (lv_name_1_0= RULE_ID ) ) (otherlv_2= 'extends' ( (otherlv_3= RULE_ID ) ) )? otherlv_4= '{' ( (lv_features_5_0= ruleFeature ) )* otherlv_6= '}' ) ;
    public final EObject ruleEntity() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        EObject lv_features_5_0 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:873:2: ( (otherlv_0= 'entity' ( (lv_name_1_0= RULE_ID ) ) (otherlv_2= 'extends' ( (otherlv_3= RULE_ID ) ) )? otherlv_4= '{' ( (lv_features_5_0= ruleFeature ) )* otherlv_6= '}' ) )
            // InternalTyphonDL.g:874:2: (otherlv_0= 'entity' ( (lv_name_1_0= RULE_ID ) ) (otherlv_2= 'extends' ( (otherlv_3= RULE_ID ) ) )? otherlv_4= '{' ( (lv_features_5_0= ruleFeature ) )* otherlv_6= '}' )
            {
            // InternalTyphonDL.g:874:2: (otherlv_0= 'entity' ( (lv_name_1_0= RULE_ID ) ) (otherlv_2= 'extends' ( (otherlv_3= RULE_ID ) ) )? otherlv_4= '{' ( (lv_features_5_0= ruleFeature ) )* otherlv_6= '}' )
            // InternalTyphonDL.g:875:3: otherlv_0= 'entity' ( (lv_name_1_0= RULE_ID ) ) (otherlv_2= 'extends' ( (otherlv_3= RULE_ID ) ) )? otherlv_4= '{' ( (lv_features_5_0= ruleFeature ) )* otherlv_6= '}'
            {
            otherlv_0=(Token)match(input,24,FOLLOW_4); 

            			newLeafNode(otherlv_0, grammarAccess.getEntityAccess().getEntityKeyword_0());
            		
            // InternalTyphonDL.g:879:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalTyphonDL.g:880:4: (lv_name_1_0= RULE_ID )
            {
            // InternalTyphonDL.g:880:4: (lv_name_1_0= RULE_ID )
            // InternalTyphonDL.g:881:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(lv_name_1_0, grammarAccess.getEntityAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getEntityRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalTyphonDL.g:897:3: (otherlv_2= 'extends' ( (otherlv_3= RULE_ID ) ) )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==25) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // InternalTyphonDL.g:898:4: otherlv_2= 'extends' ( (otherlv_3= RULE_ID ) )
                    {
                    otherlv_2=(Token)match(input,25,FOLLOW_4); 

                    				newLeafNode(otherlv_2, grammarAccess.getEntityAccess().getExtendsKeyword_2_0());
                    			
                    // InternalTyphonDL.g:902:4: ( (otherlv_3= RULE_ID ) )
                    // InternalTyphonDL.g:903:5: (otherlv_3= RULE_ID )
                    {
                    // InternalTyphonDL.g:903:5: (otherlv_3= RULE_ID )
                    // InternalTyphonDL.g:904:6: otherlv_3= RULE_ID
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getEntityRule());
                    						}
                    					
                    otherlv_3=(Token)match(input,RULE_ID,FOLLOW_6); 

                    						newLeafNode(otherlv_3, grammarAccess.getEntityAccess().getSuperTypeEntityCrossReference_2_1_0());
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_4=(Token)match(input,17,FOLLOW_10); 

            			newLeafNode(otherlv_4, grammarAccess.getEntityAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalTyphonDL.g:920:3: ( (lv_features_5_0= ruleFeature ) )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==RULE_ID) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // InternalTyphonDL.g:921:4: (lv_features_5_0= ruleFeature )
            	    {
            	    // InternalTyphonDL.g:921:4: (lv_features_5_0= ruleFeature )
            	    // InternalTyphonDL.g:922:5: lv_features_5_0= ruleFeature
            	    {

            	    					newCompositeNode(grammarAccess.getEntityAccess().getFeaturesFeatureParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_10);
            	    lv_features_5_0=ruleFeature();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getEntityRule());
            	    					}
            	    					add(
            	    						current,
            	    						"features",
            	    						lv_features_5_0,
            	    						"org.typhon.dsls.xtext.TyphonDL.Feature");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            otherlv_6=(Token)match(input,18,FOLLOW_2); 

            			newLeafNode(otherlv_6, grammarAccess.getEntityAccess().getRightCurlyBracketKeyword_5());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEntity"


    // $ANTLR start "entryRuleProperty"
    // InternalTyphonDL.g:947:1: entryRuleProperty returns [EObject current=null] : iv_ruleProperty= ruleProperty EOF ;
    public final EObject entryRuleProperty() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleProperty = null;


        try {
            // InternalTyphonDL.g:947:49: (iv_ruleProperty= ruleProperty EOF )
            // InternalTyphonDL.g:948:2: iv_ruleProperty= ruleProperty EOF
            {
             newCompositeNode(grammarAccess.getPropertyRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleProperty=ruleProperty();

            state._fsp--;

             current =iv_ruleProperty; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleProperty"


    // $ANTLR start "ruleProperty"
    // InternalTyphonDL.g:954:1: ruleProperty returns [EObject current=null] : (this_Assignment_0= ruleAssignment | this_AssignmentList_1= ruleAssignmentList | this_CommaSeparatedAssignmentList_2= ruleCommaSeparatedAssignmentList | this_EnvList_3= ruleEnvList ) ;
    public final EObject ruleProperty() throws RecognitionException {
        EObject current = null;

        EObject this_Assignment_0 = null;

        EObject this_AssignmentList_1 = null;

        EObject this_CommaSeparatedAssignmentList_2 = null;

        EObject this_EnvList_3 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:960:2: ( (this_Assignment_0= ruleAssignment | this_AssignmentList_1= ruleAssignmentList | this_CommaSeparatedAssignmentList_2= ruleCommaSeparatedAssignmentList | this_EnvList_3= ruleEnvList ) )
            // InternalTyphonDL.g:961:2: (this_Assignment_0= ruleAssignment | this_AssignmentList_1= ruleAssignmentList | this_CommaSeparatedAssignmentList_2= ruleCommaSeparatedAssignmentList | this_EnvList_3= ruleEnvList )
            {
            // InternalTyphonDL.g:961:2: (this_Assignment_0= ruleAssignment | this_AssignmentList_1= ruleAssignmentList | this_CommaSeparatedAssignmentList_2= ruleCommaSeparatedAssignmentList | this_EnvList_3= ruleEnvList )
            int alt13=4;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case 27:
                    {
                    alt13=3;
                    }
                    break;
                case 17:
                    {
                    alt13=2;
                    }
                    break;
                case 30:
                    {
                    alt13=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA13_0==26) ) {
                alt13=4;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // InternalTyphonDL.g:962:3: this_Assignment_0= ruleAssignment
                    {

                    			newCompositeNode(grammarAccess.getPropertyAccess().getAssignmentParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_Assignment_0=ruleAssignment();

                    state._fsp--;


                    			current = this_Assignment_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalTyphonDL.g:971:3: this_AssignmentList_1= ruleAssignmentList
                    {

                    			newCompositeNode(grammarAccess.getPropertyAccess().getAssignmentListParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_AssignmentList_1=ruleAssignmentList();

                    state._fsp--;


                    			current = this_AssignmentList_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalTyphonDL.g:980:3: this_CommaSeparatedAssignmentList_2= ruleCommaSeparatedAssignmentList
                    {

                    			newCompositeNode(grammarAccess.getPropertyAccess().getCommaSeparatedAssignmentListParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_CommaSeparatedAssignmentList_2=ruleCommaSeparatedAssignmentList();

                    state._fsp--;


                    			current = this_CommaSeparatedAssignmentList_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalTyphonDL.g:989:3: this_EnvList_3= ruleEnvList
                    {

                    			newCompositeNode(grammarAccess.getPropertyAccess().getEnvListParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_EnvList_3=ruleEnvList();

                    state._fsp--;


                    			current = this_EnvList_3;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleProperty"


    // $ANTLR start "entryRuleEnvList"
    // InternalTyphonDL.g:1001:1: entryRuleEnvList returns [EObject current=null] : iv_ruleEnvList= ruleEnvList EOF ;
    public final EObject entryRuleEnvList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEnvList = null;


        try {
            // InternalTyphonDL.g:1001:48: (iv_ruleEnvList= ruleEnvList EOF )
            // InternalTyphonDL.g:1002:2: iv_ruleEnvList= ruleEnvList EOF
            {
             newCompositeNode(grammarAccess.getEnvListRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEnvList=ruleEnvList();

            state._fsp--;

             current =iv_ruleEnvList; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEnvList"


    // $ANTLR start "ruleEnvList"
    // InternalTyphonDL.g:1008:1: ruleEnvList returns [EObject current=null] : (otherlv_0= 'environment' otherlv_1= '{' ( (lv_environmentVars_2_0= RULE_MYSTRING ) )+ otherlv_3= '}' ) ;
    public final EObject ruleEnvList() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token lv_environmentVars_2_0=null;
        Token otherlv_3=null;


        	enterRule();

        try {
            // InternalTyphonDL.g:1014:2: ( (otherlv_0= 'environment' otherlv_1= '{' ( (lv_environmentVars_2_0= RULE_MYSTRING ) )+ otherlv_3= '}' ) )
            // InternalTyphonDL.g:1015:2: (otherlv_0= 'environment' otherlv_1= '{' ( (lv_environmentVars_2_0= RULE_MYSTRING ) )+ otherlv_3= '}' )
            {
            // InternalTyphonDL.g:1015:2: (otherlv_0= 'environment' otherlv_1= '{' ( (lv_environmentVars_2_0= RULE_MYSTRING ) )+ otherlv_3= '}' )
            // InternalTyphonDL.g:1016:3: otherlv_0= 'environment' otherlv_1= '{' ( (lv_environmentVars_2_0= RULE_MYSTRING ) )+ otherlv_3= '}'
            {
            otherlv_0=(Token)match(input,26,FOLLOW_6); 

            			newLeafNode(otherlv_0, grammarAccess.getEnvListAccess().getEnvironmentKeyword_0());
            		
            otherlv_1=(Token)match(input,17,FOLLOW_12); 

            			newLeafNode(otherlv_1, grammarAccess.getEnvListAccess().getLeftCurlyBracketKeyword_1());
            		
            // InternalTyphonDL.g:1024:3: ( (lv_environmentVars_2_0= RULE_MYSTRING ) )+
            int cnt14=0;
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==RULE_MYSTRING) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // InternalTyphonDL.g:1025:4: (lv_environmentVars_2_0= RULE_MYSTRING )
            	    {
            	    // InternalTyphonDL.g:1025:4: (lv_environmentVars_2_0= RULE_MYSTRING )
            	    // InternalTyphonDL.g:1026:5: lv_environmentVars_2_0= RULE_MYSTRING
            	    {
            	    lv_environmentVars_2_0=(Token)match(input,RULE_MYSTRING,FOLLOW_13); 

            	    					newLeafNode(lv_environmentVars_2_0, grammarAccess.getEnvListAccess().getEnvironmentVarsMYSTRINGTerminalRuleCall_2_0());
            	    				

            	    					if (current==null) {
            	    						current = createModelElement(grammarAccess.getEnvListRule());
            	    					}
            	    					addWithLastConsumed(
            	    						current,
            	    						"environmentVars",
            	    						lv_environmentVars_2_0,
            	    						"org.typhon.dsls.xtext.TyphonDL.MYSTRING");
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt14 >= 1 ) break loop14;
                        EarlyExitException eee =
                            new EarlyExitException(14, input);
                        throw eee;
                }
                cnt14++;
            } while (true);

            otherlv_3=(Token)match(input,18,FOLLOW_2); 

            			newLeafNode(otherlv_3, grammarAccess.getEnvListAccess().getRightCurlyBracketKeyword_3());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEnvList"


    // $ANTLR start "entryRuleAssignmentList"
    // InternalTyphonDL.g:1050:1: entryRuleAssignmentList returns [EObject current=null] : iv_ruleAssignmentList= ruleAssignmentList EOF ;
    public final EObject entryRuleAssignmentList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAssignmentList = null;


        try {
            // InternalTyphonDL.g:1050:55: (iv_ruleAssignmentList= ruleAssignmentList EOF )
            // InternalTyphonDL.g:1051:2: iv_ruleAssignmentList= ruleAssignmentList EOF
            {
             newCompositeNode(grammarAccess.getAssignmentListRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAssignmentList=ruleAssignmentList();

            state._fsp--;

             current =iv_ruleAssignmentList; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAssignmentList"


    // $ANTLR start "ruleAssignmentList"
    // InternalTyphonDL.g:1057:1: ruleAssignmentList returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '{' ( (lv_assignments_2_0= ruleAssignment ) )+ otherlv_3= '}' ) ;
    public final EObject ruleAssignmentList() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_assignments_2_0 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:1063:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '{' ( (lv_assignments_2_0= ruleAssignment ) )+ otherlv_3= '}' ) )
            // InternalTyphonDL.g:1064:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '{' ( (lv_assignments_2_0= ruleAssignment ) )+ otherlv_3= '}' )
            {
            // InternalTyphonDL.g:1064:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '{' ( (lv_assignments_2_0= ruleAssignment ) )+ otherlv_3= '}' )
            // InternalTyphonDL.g:1065:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '{' ( (lv_assignments_2_0= ruleAssignment ) )+ otherlv_3= '}'
            {
            // InternalTyphonDL.g:1065:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalTyphonDL.g:1066:4: (lv_name_0_0= RULE_ID )
            {
            // InternalTyphonDL.g:1066:4: (lv_name_0_0= RULE_ID )
            // InternalTyphonDL.g:1067:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_6); 

            					newLeafNode(lv_name_0_0, grammarAccess.getAssignmentListAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAssignmentListRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,17,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getAssignmentListAccess().getLeftCurlyBracketKeyword_1());
            		
            // InternalTyphonDL.g:1087:3: ( (lv_assignments_2_0= ruleAssignment ) )+
            int cnt15=0;
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==RULE_ID) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // InternalTyphonDL.g:1088:4: (lv_assignments_2_0= ruleAssignment )
            	    {
            	    // InternalTyphonDL.g:1088:4: (lv_assignments_2_0= ruleAssignment )
            	    // InternalTyphonDL.g:1089:5: lv_assignments_2_0= ruleAssignment
            	    {

            	    					newCompositeNode(grammarAccess.getAssignmentListAccess().getAssignmentsAssignmentParserRuleCall_2_0());
            	    				
            	    pushFollow(FOLLOW_14);
            	    lv_assignments_2_0=ruleAssignment();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getAssignmentListRule());
            	    					}
            	    					add(
            	    						current,
            	    						"assignments",
            	    						lv_assignments_2_0,
            	    						"org.typhon.dsls.xtext.TyphonDL.Assignment");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt15 >= 1 ) break loop15;
                        EarlyExitException eee =
                            new EarlyExitException(15, input);
                        throw eee;
                }
                cnt15++;
            } while (true);

            otherlv_3=(Token)match(input,18,FOLLOW_2); 

            			newLeafNode(otherlv_3, grammarAccess.getAssignmentListAccess().getRightCurlyBracketKeyword_3());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAssignmentList"


    // $ANTLR start "entryRuleCommaSeparatedAssignmentList"
    // InternalTyphonDL.g:1114:1: entryRuleCommaSeparatedAssignmentList returns [EObject current=null] : iv_ruleCommaSeparatedAssignmentList= ruleCommaSeparatedAssignmentList EOF ;
    public final EObject entryRuleCommaSeparatedAssignmentList() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCommaSeparatedAssignmentList = null;


        try {
            // InternalTyphonDL.g:1114:69: (iv_ruleCommaSeparatedAssignmentList= ruleCommaSeparatedAssignmentList EOF )
            // InternalTyphonDL.g:1115:2: iv_ruleCommaSeparatedAssignmentList= ruleCommaSeparatedAssignmentList EOF
            {
             newCompositeNode(grammarAccess.getCommaSeparatedAssignmentListRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCommaSeparatedAssignmentList=ruleCommaSeparatedAssignmentList();

            state._fsp--;

             current =iv_ruleCommaSeparatedAssignmentList; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCommaSeparatedAssignmentList"


    // $ANTLR start "ruleCommaSeparatedAssignmentList"
    // InternalTyphonDL.g:1121:1: ruleCommaSeparatedAssignmentList returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '[' ( (lv_value_2_0= ruleValue ) ) (otherlv_3= ',' ( (lv_values_4_0= ruleValue ) ) )* otherlv_5= ']' ) ;
    public final EObject ruleCommaSeparatedAssignmentList() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        AntlrDatatypeRuleToken lv_value_2_0 = null;

        AntlrDatatypeRuleToken lv_values_4_0 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:1127:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '[' ( (lv_value_2_0= ruleValue ) ) (otherlv_3= ',' ( (lv_values_4_0= ruleValue ) ) )* otherlv_5= ']' ) )
            // InternalTyphonDL.g:1128:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '[' ( (lv_value_2_0= ruleValue ) ) (otherlv_3= ',' ( (lv_values_4_0= ruleValue ) ) )* otherlv_5= ']' )
            {
            // InternalTyphonDL.g:1128:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '[' ( (lv_value_2_0= ruleValue ) ) (otherlv_3= ',' ( (lv_values_4_0= ruleValue ) ) )* otherlv_5= ']' )
            // InternalTyphonDL.g:1129:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '[' ( (lv_value_2_0= ruleValue ) ) (otherlv_3= ',' ( (lv_values_4_0= ruleValue ) ) )* otherlv_5= ']'
            {
            // InternalTyphonDL.g:1129:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalTyphonDL.g:1130:4: (lv_name_0_0= RULE_ID )
            {
            // InternalTyphonDL.g:1130:4: (lv_name_0_0= RULE_ID )
            // InternalTyphonDL.g:1131:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_15); 

            					newLeafNode(lv_name_0_0, grammarAccess.getCommaSeparatedAssignmentListAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCommaSeparatedAssignmentListRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,27,FOLLOW_16); 

            			newLeafNode(otherlv_1, grammarAccess.getCommaSeparatedAssignmentListAccess().getLeftSquareBracketKeyword_1());
            		
            // InternalTyphonDL.g:1151:3: ( (lv_value_2_0= ruleValue ) )
            // InternalTyphonDL.g:1152:4: (lv_value_2_0= ruleValue )
            {
            // InternalTyphonDL.g:1152:4: (lv_value_2_0= ruleValue )
            // InternalTyphonDL.g:1153:5: lv_value_2_0= ruleValue
            {

            					newCompositeNode(grammarAccess.getCommaSeparatedAssignmentListAccess().getValueValueParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_17);
            lv_value_2_0=ruleValue();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCommaSeparatedAssignmentListRule());
            					}
            					set(
            						current,
            						"value",
            						lv_value_2_0,
            						"org.typhon.dsls.xtext.TyphonDL.Value");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalTyphonDL.g:1170:3: (otherlv_3= ',' ( (lv_values_4_0= ruleValue ) ) )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==28) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // InternalTyphonDL.g:1171:4: otherlv_3= ',' ( (lv_values_4_0= ruleValue ) )
            	    {
            	    otherlv_3=(Token)match(input,28,FOLLOW_16); 

            	    				newLeafNode(otherlv_3, grammarAccess.getCommaSeparatedAssignmentListAccess().getCommaKeyword_3_0());
            	    			
            	    // InternalTyphonDL.g:1175:4: ( (lv_values_4_0= ruleValue ) )
            	    // InternalTyphonDL.g:1176:5: (lv_values_4_0= ruleValue )
            	    {
            	    // InternalTyphonDL.g:1176:5: (lv_values_4_0= ruleValue )
            	    // InternalTyphonDL.g:1177:6: lv_values_4_0= ruleValue
            	    {

            	    						newCompositeNode(grammarAccess.getCommaSeparatedAssignmentListAccess().getValuesValueParserRuleCall_3_1_0());
            	    					
            	    pushFollow(FOLLOW_17);
            	    lv_values_4_0=ruleValue();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getCommaSeparatedAssignmentListRule());
            	    						}
            	    						add(
            	    							current,
            	    							"values",
            	    							lv_values_4_0,
            	    							"org.typhon.dsls.xtext.TyphonDL.Value");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            otherlv_5=(Token)match(input,29,FOLLOW_2); 

            			newLeafNode(otherlv_5, grammarAccess.getCommaSeparatedAssignmentListAccess().getRightSquareBracketKeyword_4());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCommaSeparatedAssignmentList"


    // $ANTLR start "entryRuleAssignment"
    // InternalTyphonDL.g:1203:1: entryRuleAssignment returns [EObject current=null] : iv_ruleAssignment= ruleAssignment EOF ;
    public final EObject entryRuleAssignment() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAssignment = null;


        try {
            // InternalTyphonDL.g:1203:51: (iv_ruleAssignment= ruleAssignment EOF )
            // InternalTyphonDL.g:1204:2: iv_ruleAssignment= ruleAssignment EOF
            {
             newCompositeNode(grammarAccess.getAssignmentRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAssignment=ruleAssignment();

            state._fsp--;

             current =iv_ruleAssignment; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAssignment"


    // $ANTLR start "ruleAssignment"
    // InternalTyphonDL.g:1210:1: ruleAssignment returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleValue ) ) ) ;
    public final EObject ruleAssignment() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_value_2_0 = null;



        	enterRule();

        try {
            // InternalTyphonDL.g:1216:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleValue ) ) ) )
            // InternalTyphonDL.g:1217:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleValue ) ) )
            {
            // InternalTyphonDL.g:1217:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleValue ) ) )
            // InternalTyphonDL.g:1218:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '=' ( (lv_value_2_0= ruleValue ) )
            {
            // InternalTyphonDL.g:1218:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalTyphonDL.g:1219:4: (lv_name_0_0= RULE_ID )
            {
            // InternalTyphonDL.g:1219:4: (lv_name_0_0= RULE_ID )
            // InternalTyphonDL.g:1220:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_18); 

            					newLeafNode(lv_name_0_0, grammarAccess.getAssignmentAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAssignmentRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,30,FOLLOW_16); 

            			newLeafNode(otherlv_1, grammarAccess.getAssignmentAccess().getEqualsSignKeyword_1());
            		
            // InternalTyphonDL.g:1240:3: ( (lv_value_2_0= ruleValue ) )
            // InternalTyphonDL.g:1241:4: (lv_value_2_0= ruleValue )
            {
            // InternalTyphonDL.g:1241:4: (lv_value_2_0= ruleValue )
            // InternalTyphonDL.g:1242:5: lv_value_2_0= ruleValue
            {

            					newCompositeNode(grammarAccess.getAssignmentAccess().getValueValueParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_2);
            lv_value_2_0=ruleValue();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAssignmentRule());
            					}
            					set(
            						current,
            						"value",
            						lv_value_2_0,
            						"org.typhon.dsls.xtext.TyphonDL.Value");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAssignment"


    // $ANTLR start "entryRuleValue"
    // InternalTyphonDL.g:1263:1: entryRuleValue returns [String current=null] : iv_ruleValue= ruleValue EOF ;
    public final String entryRuleValue() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleValue = null;


        try {
            // InternalTyphonDL.g:1263:45: (iv_ruleValue= ruleValue EOF )
            // InternalTyphonDL.g:1264:2: iv_ruleValue= ruleValue EOF
            {
             newCompositeNode(grammarAccess.getValueRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleValue=ruleValue();

            state._fsp--;

             current =iv_ruleValue.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleValue"


    // $ANTLR start "ruleValue"
    // InternalTyphonDL.g:1270:1: ruleValue returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_STRING_0= RULE_STRING | this_ID_1= RULE_ID | this_INT_2= RULE_INT | kw= '/' | kw= ':' | kw= '-' | kw= '.' )+ ;
    public final AntlrDatatypeRuleToken ruleValue() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_STRING_0=null;
        Token this_ID_1=null;
        Token this_INT_2=null;
        Token kw=null;


        	enterRule();

        try {
            // InternalTyphonDL.g:1276:2: ( (this_STRING_0= RULE_STRING | this_ID_1= RULE_ID | this_INT_2= RULE_INT | kw= '/' | kw= ':' | kw= '-' | kw= '.' )+ )
            // InternalTyphonDL.g:1277:2: (this_STRING_0= RULE_STRING | this_ID_1= RULE_ID | this_INT_2= RULE_INT | kw= '/' | kw= ':' | kw= '-' | kw= '.' )+
            {
            // InternalTyphonDL.g:1277:2: (this_STRING_0= RULE_STRING | this_ID_1= RULE_ID | this_INT_2= RULE_INT | kw= '/' | kw= ':' | kw= '-' | kw= '.' )+
            int cnt17=0;
            loop17:
            do {
                int alt17=8;
                alt17 = dfa17.predict(input);
                switch (alt17) {
            	case 1 :
            	    // InternalTyphonDL.g:1278:3: this_STRING_0= RULE_STRING
            	    {
            	    this_STRING_0=(Token)match(input,RULE_STRING,FOLLOW_19); 

            	    			current.merge(this_STRING_0);
            	    		

            	    			newLeafNode(this_STRING_0, grammarAccess.getValueAccess().getSTRINGTerminalRuleCall_0());
            	    		

            	    }
            	    break;
            	case 2 :
            	    // InternalTyphonDL.g:1286:3: this_ID_1= RULE_ID
            	    {
            	    this_ID_1=(Token)match(input,RULE_ID,FOLLOW_19); 

            	    			current.merge(this_ID_1);
            	    		

            	    			newLeafNode(this_ID_1, grammarAccess.getValueAccess().getIDTerminalRuleCall_1());
            	    		

            	    }
            	    break;
            	case 3 :
            	    // InternalTyphonDL.g:1294:3: this_INT_2= RULE_INT
            	    {
            	    this_INT_2=(Token)match(input,RULE_INT,FOLLOW_19); 

            	    			current.merge(this_INT_2);
            	    		

            	    			newLeafNode(this_INT_2, grammarAccess.getValueAccess().getINTTerminalRuleCall_2());
            	    		

            	    }
            	    break;
            	case 4 :
            	    // InternalTyphonDL.g:1302:3: kw= '/'
            	    {
            	    kw=(Token)match(input,31,FOLLOW_19); 

            	    			current.merge(kw);
            	    			newLeafNode(kw, grammarAccess.getValueAccess().getSolidusKeyword_3());
            	    		

            	    }
            	    break;
            	case 5 :
            	    // InternalTyphonDL.g:1308:3: kw= ':'
            	    {
            	    kw=(Token)match(input,16,FOLLOW_19); 

            	    			current.merge(kw);
            	    			newLeafNode(kw, grammarAccess.getValueAccess().getColonKeyword_4());
            	    		

            	    }
            	    break;
            	case 6 :
            	    // InternalTyphonDL.g:1314:3: kw= '-'
            	    {
            	    kw=(Token)match(input,32,FOLLOW_19); 

            	    			current.merge(kw);
            	    			newLeafNode(kw, grammarAccess.getValueAccess().getHyphenMinusKeyword_5());
            	    		

            	    }
            	    break;
            	case 7 :
            	    // InternalTyphonDL.g:1320:3: kw= '.'
            	    {
            	    kw=(Token)match(input,33,FOLLOW_19); 

            	    			current.merge(kw);
            	    			newLeafNode(kw, grammarAccess.getValueAccess().getFullStopKeyword_6());
            	    		

            	    }
            	    break;

            	default :
            	    if ( cnt17 >= 1 ) break loop17;
                        EarlyExitException eee =
                            new EarlyExitException(17, input);
                        throw eee;
                }
                cnt17++;
            } while (true);


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleValue"


    // $ANTLR start "entryRuleFeature"
    // InternalTyphonDL.g:1329:1: entryRuleFeature returns [EObject current=null] : iv_ruleFeature= ruleFeature EOF ;
    public final EObject entryRuleFeature() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFeature = null;


        try {
            // InternalTyphonDL.g:1329:48: (iv_ruleFeature= ruleFeature EOF )
            // InternalTyphonDL.g:1330:2: iv_ruleFeature= ruleFeature EOF
            {
             newCompositeNode(grammarAccess.getFeatureRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFeature=ruleFeature();

            state._fsp--;

             current =iv_ruleFeature; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFeature"


    // $ANTLR start "ruleFeature"
    // InternalTyphonDL.g:1336:1: ruleFeature returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( (otherlv_2= RULE_ID ) ) ) ;
    public final EObject ruleFeature() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;


        	enterRule();

        try {
            // InternalTyphonDL.g:1342:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( (otherlv_2= RULE_ID ) ) ) )
            // InternalTyphonDL.g:1343:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( (otherlv_2= RULE_ID ) ) )
            {
            // InternalTyphonDL.g:1343:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( (otherlv_2= RULE_ID ) ) )
            // InternalTyphonDL.g:1344:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( (otherlv_2= RULE_ID ) )
            {
            // InternalTyphonDL.g:1344:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalTyphonDL.g:1345:4: (lv_name_0_0= RULE_ID )
            {
            // InternalTyphonDL.g:1345:4: (lv_name_0_0= RULE_ID )
            // InternalTyphonDL.g:1346:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            					newLeafNode(lv_name_0_0, grammarAccess.getFeatureAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getFeatureRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,16,FOLLOW_4); 

            			newLeafNode(otherlv_1, grammarAccess.getFeatureAccess().getColonKeyword_1());
            		
            // InternalTyphonDL.g:1366:3: ( (otherlv_2= RULE_ID ) )
            // InternalTyphonDL.g:1367:4: (otherlv_2= RULE_ID )
            {
            // InternalTyphonDL.g:1367:4: (otherlv_2= RULE_ID )
            // InternalTyphonDL.g:1368:5: otherlv_2= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getFeatureRule());
            					}
            				
            otherlv_2=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(otherlv_2, grammarAccess.getFeatureAccess().getTypeTypeCrossReference_2_0());
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFeature"

    // Delegated rules


    protected DFA17 dfa17 = new DFA17(this);
    static final String dfa_1s = "\14\uffff";
    static final String dfa_2s = "\1\1\1\uffff\1\11\7\uffff\1\13\1\uffff";
    static final String dfa_3s = "\1\4\1\uffff\1\4\7\uffff\1\4\1\uffff";
    static final String dfa_4s = "\1\41\1\uffff\1\41\7\uffff\1\41\1\uffff";
    static final String dfa_5s = "\1\uffff\1\10\1\uffff\1\1\1\3\1\4\1\5\1\6\1\7\1\2\1\uffff\1\2";
    static final String dfa_6s = "\14\uffff}>";
    static final String[] dfa_7s = {
            "\1\2\1\uffff\1\3\1\4\10\uffff\1\6\1\uffff\1\1\7\uffff\1\1\1\uffff\2\1\1\uffff\1\5\1\7\1\10",
            "",
            "\1\11\1\uffff\2\11\10\uffff\1\12\1\1\1\11\7\uffff\1\11\1\1\2\11\1\1\3\11",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\13\1\uffff\2\13\10\uffff\1\13\1\uffff\1\13\7\uffff\1\13\1\uffff\1\11\1\13\1\uffff\3\13",
            ""
    };

    static final short[] dfa_1 = DFA.unpackEncodedString(dfa_1s);
    static final short[] dfa_2 = DFA.unpackEncodedString(dfa_2s);
    static final char[] dfa_3 = DFA.unpackEncodedStringToUnsignedChars(dfa_3s);
    static final char[] dfa_4 = DFA.unpackEncodedStringToUnsignedChars(dfa_4s);
    static final short[] dfa_5 = DFA.unpackEncodedString(dfa_5s);
    static final short[] dfa_6 = DFA.unpackEncodedString(dfa_6s);
    static final short[][] dfa_7 = unpackEncodedStringArray(dfa_7s);

    class DFA17 extends DFA {

        public DFA17(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 17;
            this.eot = dfa_1;
            this.eof = dfa_2;
            this.min = dfa_3;
            this.max = dfa_4;
            this.accept = dfa_5;
            this.special = dfa_6;
            this.transition = dfa_7;
        }
        public String getDescription() {
            return "()+ loopback of 1277:2: (this_STRING_0= RULE_STRING | this_ID_1= RULE_ID | this_INT_2= RULE_INT | kw= '/' | kw= ':' | kw= '-' | kw= '.' )+";
        }
    }
 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000001F8F002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x00000000000C0000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000140000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000240000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000004040010L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000002020000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000040020L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000000040010L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x00000003800100D0L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000030000000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x00000003800100D2L});

}