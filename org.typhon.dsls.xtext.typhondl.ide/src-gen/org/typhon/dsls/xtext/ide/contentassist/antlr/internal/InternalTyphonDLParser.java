package org.typhon.dsls.xtext.ide.contentassist.antlr.internal;

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.DFA;
import org.typhon.dsls.xtext.services.TyphonDLGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTyphonDLParser extends AbstractInternalContentAssistParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_STRING", "RULE_ID", "RULE_INT", "RULE_MYSTRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'/'", "':'", "'-'", "'.'", "'datatype'", "'platformtype'", "'dbtype'", "'platform'", "'{'", "'}'", "'cluster'", "'application'", "'container'", "'dbService'", "'businessService'", "'entity'", "'extends'", "'environment'", "'['", "']'", "','", "'='"
    };
    public static final int RULE_STRING=4;
    public static final int RULE_SL_COMMENT=9;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int RULE_MYSTRING=7;
    public static final int T__33=33;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int RULE_ID=5;
    public static final int RULE_WS=10;
    public static final int RULE_ANY_OTHER=11;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=6;
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

    	public void setGrammarAccess(TyphonDLGrammarAccess grammarAccess) {
    		this.grammarAccess = grammarAccess;
    	}

    	@Override
    	protected Grammar getGrammar() {
    		return grammarAccess.getGrammar();
    	}

    	@Override
    	protected String getValueForTokenName(String tokenName) {
    		return tokenName;
    	}



    // $ANTLR start "entryRuleDeploymentModel"
    // InternalTyphonDL.g:53:1: entryRuleDeploymentModel : ruleDeploymentModel EOF ;
    public final void entryRuleDeploymentModel() throws RecognitionException {
        try {
            // InternalTyphonDL.g:54:1: ( ruleDeploymentModel EOF )
            // InternalTyphonDL.g:55:1: ruleDeploymentModel EOF
            {
             before(grammarAccess.getDeploymentModelRule()); 
            pushFollow(FOLLOW_1);
            ruleDeploymentModel();

            state._fsp--;

             after(grammarAccess.getDeploymentModelRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleDeploymentModel"


    // $ANTLR start "ruleDeploymentModel"
    // InternalTyphonDL.g:62:1: ruleDeploymentModel : ( ( rule__DeploymentModel__ElementsAssignment )* ) ;
    public final void ruleDeploymentModel() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:66:2: ( ( ( rule__DeploymentModel__ElementsAssignment )* ) )
            // InternalTyphonDL.g:67:2: ( ( rule__DeploymentModel__ElementsAssignment )* )
            {
            // InternalTyphonDL.g:67:2: ( ( rule__DeploymentModel__ElementsAssignment )* )
            // InternalTyphonDL.g:68:3: ( rule__DeploymentModel__ElementsAssignment )*
            {
             before(grammarAccess.getDeploymentModelAccess().getElementsAssignment()); 
            // InternalTyphonDL.g:69:3: ( rule__DeploymentModel__ElementsAssignment )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=16 && LA1_0<=19)||(LA1_0>=22 && LA1_0<=27)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalTyphonDL.g:69:4: rule__DeploymentModel__ElementsAssignment
            	    {
            	    pushFollow(FOLLOW_3);
            	    rule__DeploymentModel__ElementsAssignment();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

             after(grammarAccess.getDeploymentModelAccess().getElementsAssignment()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleDeploymentModel"


    // $ANTLR start "entryRuleType"
    // InternalTyphonDL.g:78:1: entryRuleType : ruleType EOF ;
    public final void entryRuleType() throws RecognitionException {
        try {
            // InternalTyphonDL.g:79:1: ( ruleType EOF )
            // InternalTyphonDL.g:80:1: ruleType EOF
            {
             before(grammarAccess.getTypeRule()); 
            pushFollow(FOLLOW_1);
            ruleType();

            state._fsp--;

             after(grammarAccess.getTypeRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleType"


    // $ANTLR start "ruleType"
    // InternalTyphonDL.g:87:1: ruleType : ( ( rule__Type__Alternatives ) ) ;
    public final void ruleType() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:91:2: ( ( ( rule__Type__Alternatives ) ) )
            // InternalTyphonDL.g:92:2: ( ( rule__Type__Alternatives ) )
            {
            // InternalTyphonDL.g:92:2: ( ( rule__Type__Alternatives ) )
            // InternalTyphonDL.g:93:3: ( rule__Type__Alternatives )
            {
             before(grammarAccess.getTypeAccess().getAlternatives()); 
            // InternalTyphonDL.g:94:3: ( rule__Type__Alternatives )
            // InternalTyphonDL.g:94:4: rule__Type__Alternatives
            {
            pushFollow(FOLLOW_2);
            rule__Type__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getTypeAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleType"


    // $ANTLR start "entryRuleDataType"
    // InternalTyphonDL.g:103:1: entryRuleDataType : ruleDataType EOF ;
    public final void entryRuleDataType() throws RecognitionException {
        try {
            // InternalTyphonDL.g:104:1: ( ruleDataType EOF )
            // InternalTyphonDL.g:105:1: ruleDataType EOF
            {
             before(grammarAccess.getDataTypeRule()); 
            pushFollow(FOLLOW_1);
            ruleDataType();

            state._fsp--;

             after(grammarAccess.getDataTypeRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleDataType"


    // $ANTLR start "ruleDataType"
    // InternalTyphonDL.g:112:1: ruleDataType : ( ( rule__DataType__Group__0 ) ) ;
    public final void ruleDataType() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:116:2: ( ( ( rule__DataType__Group__0 ) ) )
            // InternalTyphonDL.g:117:2: ( ( rule__DataType__Group__0 ) )
            {
            // InternalTyphonDL.g:117:2: ( ( rule__DataType__Group__0 ) )
            // InternalTyphonDL.g:118:3: ( rule__DataType__Group__0 )
            {
             before(grammarAccess.getDataTypeAccess().getGroup()); 
            // InternalTyphonDL.g:119:3: ( rule__DataType__Group__0 )
            // InternalTyphonDL.g:119:4: rule__DataType__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__DataType__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getDataTypeAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleDataType"


    // $ANTLR start "entryRulePlatformType"
    // InternalTyphonDL.g:128:1: entryRulePlatformType : rulePlatformType EOF ;
    public final void entryRulePlatformType() throws RecognitionException {
        try {
            // InternalTyphonDL.g:129:1: ( rulePlatformType EOF )
            // InternalTyphonDL.g:130:1: rulePlatformType EOF
            {
             before(grammarAccess.getPlatformTypeRule()); 
            pushFollow(FOLLOW_1);
            rulePlatformType();

            state._fsp--;

             after(grammarAccess.getPlatformTypeRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRulePlatformType"


    // $ANTLR start "rulePlatformType"
    // InternalTyphonDL.g:137:1: rulePlatformType : ( ( rule__PlatformType__Group__0 ) ) ;
    public final void rulePlatformType() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:141:2: ( ( ( rule__PlatformType__Group__0 ) ) )
            // InternalTyphonDL.g:142:2: ( ( rule__PlatformType__Group__0 ) )
            {
            // InternalTyphonDL.g:142:2: ( ( rule__PlatformType__Group__0 ) )
            // InternalTyphonDL.g:143:3: ( rule__PlatformType__Group__0 )
            {
             before(grammarAccess.getPlatformTypeAccess().getGroup()); 
            // InternalTyphonDL.g:144:3: ( rule__PlatformType__Group__0 )
            // InternalTyphonDL.g:144:4: rule__PlatformType__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__PlatformType__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getPlatformTypeAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rulePlatformType"


    // $ANTLR start "entryRuleDBType"
    // InternalTyphonDL.g:153:1: entryRuleDBType : ruleDBType EOF ;
    public final void entryRuleDBType() throws RecognitionException {
        try {
            // InternalTyphonDL.g:154:1: ( ruleDBType EOF )
            // InternalTyphonDL.g:155:1: ruleDBType EOF
            {
             before(grammarAccess.getDBTypeRule()); 
            pushFollow(FOLLOW_1);
            ruleDBType();

            state._fsp--;

             after(grammarAccess.getDBTypeRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleDBType"


    // $ANTLR start "ruleDBType"
    // InternalTyphonDL.g:162:1: ruleDBType : ( ( rule__DBType__Group__0 ) ) ;
    public final void ruleDBType() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:166:2: ( ( ( rule__DBType__Group__0 ) ) )
            // InternalTyphonDL.g:167:2: ( ( rule__DBType__Group__0 ) )
            {
            // InternalTyphonDL.g:167:2: ( ( rule__DBType__Group__0 ) )
            // InternalTyphonDL.g:168:3: ( rule__DBType__Group__0 )
            {
             before(grammarAccess.getDBTypeAccess().getGroup()); 
            // InternalTyphonDL.g:169:3: ( rule__DBType__Group__0 )
            // InternalTyphonDL.g:169:4: rule__DBType__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__DBType__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getDBTypeAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleDBType"


    // $ANTLR start "entryRuleDeployment"
    // InternalTyphonDL.g:178:1: entryRuleDeployment : ruleDeployment EOF ;
    public final void entryRuleDeployment() throws RecognitionException {
        try {
            // InternalTyphonDL.g:179:1: ( ruleDeployment EOF )
            // InternalTyphonDL.g:180:1: ruleDeployment EOF
            {
             before(grammarAccess.getDeploymentRule()); 
            pushFollow(FOLLOW_1);
            ruleDeployment();

            state._fsp--;

             after(grammarAccess.getDeploymentRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleDeployment"


    // $ANTLR start "ruleDeployment"
    // InternalTyphonDL.g:187:1: ruleDeployment : ( ( rule__Deployment__Alternatives ) ) ;
    public final void ruleDeployment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:191:2: ( ( ( rule__Deployment__Alternatives ) ) )
            // InternalTyphonDL.g:192:2: ( ( rule__Deployment__Alternatives ) )
            {
            // InternalTyphonDL.g:192:2: ( ( rule__Deployment__Alternatives ) )
            // InternalTyphonDL.g:193:3: ( rule__Deployment__Alternatives )
            {
             before(grammarAccess.getDeploymentAccess().getAlternatives()); 
            // InternalTyphonDL.g:194:3: ( rule__Deployment__Alternatives )
            // InternalTyphonDL.g:194:4: rule__Deployment__Alternatives
            {
            pushFollow(FOLLOW_2);
            rule__Deployment__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getDeploymentAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleDeployment"


    // $ANTLR start "entryRulePlatform"
    // InternalTyphonDL.g:203:1: entryRulePlatform : rulePlatform EOF ;
    public final void entryRulePlatform() throws RecognitionException {
        try {
            // InternalTyphonDL.g:204:1: ( rulePlatform EOF )
            // InternalTyphonDL.g:205:1: rulePlatform EOF
            {
             before(grammarAccess.getPlatformRule()); 
            pushFollow(FOLLOW_1);
            rulePlatform();

            state._fsp--;

             after(grammarAccess.getPlatformRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRulePlatform"


    // $ANTLR start "rulePlatform"
    // InternalTyphonDL.g:212:1: rulePlatform : ( ( rule__Platform__Group__0 ) ) ;
    public final void rulePlatform() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:216:2: ( ( ( rule__Platform__Group__0 ) ) )
            // InternalTyphonDL.g:217:2: ( ( rule__Platform__Group__0 ) )
            {
            // InternalTyphonDL.g:217:2: ( ( rule__Platform__Group__0 ) )
            // InternalTyphonDL.g:218:3: ( rule__Platform__Group__0 )
            {
             before(grammarAccess.getPlatformAccess().getGroup()); 
            // InternalTyphonDL.g:219:3: ( rule__Platform__Group__0 )
            // InternalTyphonDL.g:219:4: rule__Platform__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Platform__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getPlatformAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rulePlatform"


    // $ANTLR start "entryRuleCluster"
    // InternalTyphonDL.g:228:1: entryRuleCluster : ruleCluster EOF ;
    public final void entryRuleCluster() throws RecognitionException {
        try {
            // InternalTyphonDL.g:229:1: ( ruleCluster EOF )
            // InternalTyphonDL.g:230:1: ruleCluster EOF
            {
             before(grammarAccess.getClusterRule()); 
            pushFollow(FOLLOW_1);
            ruleCluster();

            state._fsp--;

             after(grammarAccess.getClusterRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleCluster"


    // $ANTLR start "ruleCluster"
    // InternalTyphonDL.g:237:1: ruleCluster : ( ( rule__Cluster__Group__0 ) ) ;
    public final void ruleCluster() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:241:2: ( ( ( rule__Cluster__Group__0 ) ) )
            // InternalTyphonDL.g:242:2: ( ( rule__Cluster__Group__0 ) )
            {
            // InternalTyphonDL.g:242:2: ( ( rule__Cluster__Group__0 ) )
            // InternalTyphonDL.g:243:3: ( rule__Cluster__Group__0 )
            {
             before(grammarAccess.getClusterAccess().getGroup()); 
            // InternalTyphonDL.g:244:3: ( rule__Cluster__Group__0 )
            // InternalTyphonDL.g:244:4: rule__Cluster__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Cluster__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getClusterAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleCluster"


    // $ANTLR start "entryRuleApplication"
    // InternalTyphonDL.g:253:1: entryRuleApplication : ruleApplication EOF ;
    public final void entryRuleApplication() throws RecognitionException {
        try {
            // InternalTyphonDL.g:254:1: ( ruleApplication EOF )
            // InternalTyphonDL.g:255:1: ruleApplication EOF
            {
             before(grammarAccess.getApplicationRule()); 
            pushFollow(FOLLOW_1);
            ruleApplication();

            state._fsp--;

             after(grammarAccess.getApplicationRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleApplication"


    // $ANTLR start "ruleApplication"
    // InternalTyphonDL.g:262:1: ruleApplication : ( ( rule__Application__Group__0 ) ) ;
    public final void ruleApplication() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:266:2: ( ( ( rule__Application__Group__0 ) ) )
            // InternalTyphonDL.g:267:2: ( ( rule__Application__Group__0 ) )
            {
            // InternalTyphonDL.g:267:2: ( ( rule__Application__Group__0 ) )
            // InternalTyphonDL.g:268:3: ( rule__Application__Group__0 )
            {
             before(grammarAccess.getApplicationAccess().getGroup()); 
            // InternalTyphonDL.g:269:3: ( rule__Application__Group__0 )
            // InternalTyphonDL.g:269:4: rule__Application__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Application__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getApplicationAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleApplication"


    // $ANTLR start "entryRuleContainer"
    // InternalTyphonDL.g:278:1: entryRuleContainer : ruleContainer EOF ;
    public final void entryRuleContainer() throws RecognitionException {
        try {
            // InternalTyphonDL.g:279:1: ( ruleContainer EOF )
            // InternalTyphonDL.g:280:1: ruleContainer EOF
            {
             before(grammarAccess.getContainerRule()); 
            pushFollow(FOLLOW_1);
            ruleContainer();

            state._fsp--;

             after(grammarAccess.getContainerRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleContainer"


    // $ANTLR start "ruleContainer"
    // InternalTyphonDL.g:287:1: ruleContainer : ( ( rule__Container__Group__0 ) ) ;
    public final void ruleContainer() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:291:2: ( ( ( rule__Container__Group__0 ) ) )
            // InternalTyphonDL.g:292:2: ( ( rule__Container__Group__0 ) )
            {
            // InternalTyphonDL.g:292:2: ( ( rule__Container__Group__0 ) )
            // InternalTyphonDL.g:293:3: ( rule__Container__Group__0 )
            {
             before(grammarAccess.getContainerAccess().getGroup()); 
            // InternalTyphonDL.g:294:3: ( rule__Container__Group__0 )
            // InternalTyphonDL.g:294:4: rule__Container__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Container__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getContainerAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleContainer"


    // $ANTLR start "entryRuleService"
    // InternalTyphonDL.g:303:1: entryRuleService : ruleService EOF ;
    public final void entryRuleService() throws RecognitionException {
        try {
            // InternalTyphonDL.g:304:1: ( ruleService EOF )
            // InternalTyphonDL.g:305:1: ruleService EOF
            {
             before(grammarAccess.getServiceRule()); 
            pushFollow(FOLLOW_1);
            ruleService();

            state._fsp--;

             after(grammarAccess.getServiceRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleService"


    // $ANTLR start "ruleService"
    // InternalTyphonDL.g:312:1: ruleService : ( ( rule__Service__Alternatives ) ) ;
    public final void ruleService() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:316:2: ( ( ( rule__Service__Alternatives ) ) )
            // InternalTyphonDL.g:317:2: ( ( rule__Service__Alternatives ) )
            {
            // InternalTyphonDL.g:317:2: ( ( rule__Service__Alternatives ) )
            // InternalTyphonDL.g:318:3: ( rule__Service__Alternatives )
            {
             before(grammarAccess.getServiceAccess().getAlternatives()); 
            // InternalTyphonDL.g:319:3: ( rule__Service__Alternatives )
            // InternalTyphonDL.g:319:4: rule__Service__Alternatives
            {
            pushFollow(FOLLOW_2);
            rule__Service__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getServiceAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleService"


    // $ANTLR start "entryRuleDBService"
    // InternalTyphonDL.g:328:1: entryRuleDBService : ruleDBService EOF ;
    public final void entryRuleDBService() throws RecognitionException {
        try {
            // InternalTyphonDL.g:329:1: ( ruleDBService EOF )
            // InternalTyphonDL.g:330:1: ruleDBService EOF
            {
             before(grammarAccess.getDBServiceRule()); 
            pushFollow(FOLLOW_1);
            ruleDBService();

            state._fsp--;

             after(grammarAccess.getDBServiceRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleDBService"


    // $ANTLR start "ruleDBService"
    // InternalTyphonDL.g:337:1: ruleDBService : ( ( rule__DBService__Group__0 ) ) ;
    public final void ruleDBService() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:341:2: ( ( ( rule__DBService__Group__0 ) ) )
            // InternalTyphonDL.g:342:2: ( ( rule__DBService__Group__0 ) )
            {
            // InternalTyphonDL.g:342:2: ( ( rule__DBService__Group__0 ) )
            // InternalTyphonDL.g:343:3: ( rule__DBService__Group__0 )
            {
             before(grammarAccess.getDBServiceAccess().getGroup()); 
            // InternalTyphonDL.g:344:3: ( rule__DBService__Group__0 )
            // InternalTyphonDL.g:344:4: rule__DBService__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__DBService__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getDBServiceAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleDBService"


    // $ANTLR start "entryRuleBusinessService"
    // InternalTyphonDL.g:353:1: entryRuleBusinessService : ruleBusinessService EOF ;
    public final void entryRuleBusinessService() throws RecognitionException {
        try {
            // InternalTyphonDL.g:354:1: ( ruleBusinessService EOF )
            // InternalTyphonDL.g:355:1: ruleBusinessService EOF
            {
             before(grammarAccess.getBusinessServiceRule()); 
            pushFollow(FOLLOW_1);
            ruleBusinessService();

            state._fsp--;

             after(grammarAccess.getBusinessServiceRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleBusinessService"


    // $ANTLR start "ruleBusinessService"
    // InternalTyphonDL.g:362:1: ruleBusinessService : ( ( rule__BusinessService__Group__0 ) ) ;
    public final void ruleBusinessService() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:366:2: ( ( ( rule__BusinessService__Group__0 ) ) )
            // InternalTyphonDL.g:367:2: ( ( rule__BusinessService__Group__0 ) )
            {
            // InternalTyphonDL.g:367:2: ( ( rule__BusinessService__Group__0 ) )
            // InternalTyphonDL.g:368:3: ( rule__BusinessService__Group__0 )
            {
             before(grammarAccess.getBusinessServiceAccess().getGroup()); 
            // InternalTyphonDL.g:369:3: ( rule__BusinessService__Group__0 )
            // InternalTyphonDL.g:369:4: rule__BusinessService__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__BusinessService__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getBusinessServiceAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleBusinessService"


    // $ANTLR start "entryRuleEntity"
    // InternalTyphonDL.g:378:1: entryRuleEntity : ruleEntity EOF ;
    public final void entryRuleEntity() throws RecognitionException {
        try {
            // InternalTyphonDL.g:379:1: ( ruleEntity EOF )
            // InternalTyphonDL.g:380:1: ruleEntity EOF
            {
             before(grammarAccess.getEntityRule()); 
            pushFollow(FOLLOW_1);
            ruleEntity();

            state._fsp--;

             after(grammarAccess.getEntityRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleEntity"


    // $ANTLR start "ruleEntity"
    // InternalTyphonDL.g:387:1: ruleEntity : ( ( rule__Entity__Group__0 ) ) ;
    public final void ruleEntity() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:391:2: ( ( ( rule__Entity__Group__0 ) ) )
            // InternalTyphonDL.g:392:2: ( ( rule__Entity__Group__0 ) )
            {
            // InternalTyphonDL.g:392:2: ( ( rule__Entity__Group__0 ) )
            // InternalTyphonDL.g:393:3: ( rule__Entity__Group__0 )
            {
             before(grammarAccess.getEntityAccess().getGroup()); 
            // InternalTyphonDL.g:394:3: ( rule__Entity__Group__0 )
            // InternalTyphonDL.g:394:4: rule__Entity__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Entity__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getEntityAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleEntity"


    // $ANTLR start "entryRuleProperty"
    // InternalTyphonDL.g:403:1: entryRuleProperty : ruleProperty EOF ;
    public final void entryRuleProperty() throws RecognitionException {
        try {
            // InternalTyphonDL.g:404:1: ( ruleProperty EOF )
            // InternalTyphonDL.g:405:1: ruleProperty EOF
            {
             before(grammarAccess.getPropertyRule()); 
            pushFollow(FOLLOW_1);
            ruleProperty();

            state._fsp--;

             after(grammarAccess.getPropertyRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleProperty"


    // $ANTLR start "ruleProperty"
    // InternalTyphonDL.g:412:1: ruleProperty : ( ( rule__Property__Alternatives ) ) ;
    public final void ruleProperty() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:416:2: ( ( ( rule__Property__Alternatives ) ) )
            // InternalTyphonDL.g:417:2: ( ( rule__Property__Alternatives ) )
            {
            // InternalTyphonDL.g:417:2: ( ( rule__Property__Alternatives ) )
            // InternalTyphonDL.g:418:3: ( rule__Property__Alternatives )
            {
             before(grammarAccess.getPropertyAccess().getAlternatives()); 
            // InternalTyphonDL.g:419:3: ( rule__Property__Alternatives )
            // InternalTyphonDL.g:419:4: rule__Property__Alternatives
            {
            pushFollow(FOLLOW_2);
            rule__Property__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getPropertyAccess().getAlternatives()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleProperty"


    // $ANTLR start "entryRuleEnvList"
    // InternalTyphonDL.g:428:1: entryRuleEnvList : ruleEnvList EOF ;
    public final void entryRuleEnvList() throws RecognitionException {
        try {
            // InternalTyphonDL.g:429:1: ( ruleEnvList EOF )
            // InternalTyphonDL.g:430:1: ruleEnvList EOF
            {
             before(grammarAccess.getEnvListRule()); 
            pushFollow(FOLLOW_1);
            ruleEnvList();

            state._fsp--;

             after(grammarAccess.getEnvListRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleEnvList"


    // $ANTLR start "ruleEnvList"
    // InternalTyphonDL.g:437:1: ruleEnvList : ( ( rule__EnvList__Group__0 ) ) ;
    public final void ruleEnvList() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:441:2: ( ( ( rule__EnvList__Group__0 ) ) )
            // InternalTyphonDL.g:442:2: ( ( rule__EnvList__Group__0 ) )
            {
            // InternalTyphonDL.g:442:2: ( ( rule__EnvList__Group__0 ) )
            // InternalTyphonDL.g:443:3: ( rule__EnvList__Group__0 )
            {
             before(grammarAccess.getEnvListAccess().getGroup()); 
            // InternalTyphonDL.g:444:3: ( rule__EnvList__Group__0 )
            // InternalTyphonDL.g:444:4: rule__EnvList__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__EnvList__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getEnvListAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleEnvList"


    // $ANTLR start "entryRuleAssignmentList"
    // InternalTyphonDL.g:453:1: entryRuleAssignmentList : ruleAssignmentList EOF ;
    public final void entryRuleAssignmentList() throws RecognitionException {
        try {
            // InternalTyphonDL.g:454:1: ( ruleAssignmentList EOF )
            // InternalTyphonDL.g:455:1: ruleAssignmentList EOF
            {
             before(grammarAccess.getAssignmentListRule()); 
            pushFollow(FOLLOW_1);
            ruleAssignmentList();

            state._fsp--;

             after(grammarAccess.getAssignmentListRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleAssignmentList"


    // $ANTLR start "ruleAssignmentList"
    // InternalTyphonDL.g:462:1: ruleAssignmentList : ( ( rule__AssignmentList__Group__0 ) ) ;
    public final void ruleAssignmentList() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:466:2: ( ( ( rule__AssignmentList__Group__0 ) ) )
            // InternalTyphonDL.g:467:2: ( ( rule__AssignmentList__Group__0 ) )
            {
            // InternalTyphonDL.g:467:2: ( ( rule__AssignmentList__Group__0 ) )
            // InternalTyphonDL.g:468:3: ( rule__AssignmentList__Group__0 )
            {
             before(grammarAccess.getAssignmentListAccess().getGroup()); 
            // InternalTyphonDL.g:469:3: ( rule__AssignmentList__Group__0 )
            // InternalTyphonDL.g:469:4: rule__AssignmentList__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__AssignmentList__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getAssignmentListAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleAssignmentList"


    // $ANTLR start "entryRuleCommaSeparatedAssignmentList"
    // InternalTyphonDL.g:478:1: entryRuleCommaSeparatedAssignmentList : ruleCommaSeparatedAssignmentList EOF ;
    public final void entryRuleCommaSeparatedAssignmentList() throws RecognitionException {
        try {
            // InternalTyphonDL.g:479:1: ( ruleCommaSeparatedAssignmentList EOF )
            // InternalTyphonDL.g:480:1: ruleCommaSeparatedAssignmentList EOF
            {
             before(grammarAccess.getCommaSeparatedAssignmentListRule()); 
            pushFollow(FOLLOW_1);
            ruleCommaSeparatedAssignmentList();

            state._fsp--;

             after(grammarAccess.getCommaSeparatedAssignmentListRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleCommaSeparatedAssignmentList"


    // $ANTLR start "ruleCommaSeparatedAssignmentList"
    // InternalTyphonDL.g:487:1: ruleCommaSeparatedAssignmentList : ( ( rule__CommaSeparatedAssignmentList__Group__0 ) ) ;
    public final void ruleCommaSeparatedAssignmentList() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:491:2: ( ( ( rule__CommaSeparatedAssignmentList__Group__0 ) ) )
            // InternalTyphonDL.g:492:2: ( ( rule__CommaSeparatedAssignmentList__Group__0 ) )
            {
            // InternalTyphonDL.g:492:2: ( ( rule__CommaSeparatedAssignmentList__Group__0 ) )
            // InternalTyphonDL.g:493:3: ( rule__CommaSeparatedAssignmentList__Group__0 )
            {
             before(grammarAccess.getCommaSeparatedAssignmentListAccess().getGroup()); 
            // InternalTyphonDL.g:494:3: ( rule__CommaSeparatedAssignmentList__Group__0 )
            // InternalTyphonDL.g:494:4: rule__CommaSeparatedAssignmentList__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__CommaSeparatedAssignmentList__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getCommaSeparatedAssignmentListAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleCommaSeparatedAssignmentList"


    // $ANTLR start "entryRuleAssignment"
    // InternalTyphonDL.g:503:1: entryRuleAssignment : ruleAssignment EOF ;
    public final void entryRuleAssignment() throws RecognitionException {
        try {
            // InternalTyphonDL.g:504:1: ( ruleAssignment EOF )
            // InternalTyphonDL.g:505:1: ruleAssignment EOF
            {
             before(grammarAccess.getAssignmentRule()); 
            pushFollow(FOLLOW_1);
            ruleAssignment();

            state._fsp--;

             after(grammarAccess.getAssignmentRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleAssignment"


    // $ANTLR start "ruleAssignment"
    // InternalTyphonDL.g:512:1: ruleAssignment : ( ( rule__Assignment__Group__0 ) ) ;
    public final void ruleAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:516:2: ( ( ( rule__Assignment__Group__0 ) ) )
            // InternalTyphonDL.g:517:2: ( ( rule__Assignment__Group__0 ) )
            {
            // InternalTyphonDL.g:517:2: ( ( rule__Assignment__Group__0 ) )
            // InternalTyphonDL.g:518:3: ( rule__Assignment__Group__0 )
            {
             before(grammarAccess.getAssignmentAccess().getGroup()); 
            // InternalTyphonDL.g:519:3: ( rule__Assignment__Group__0 )
            // InternalTyphonDL.g:519:4: rule__Assignment__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Assignment__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getAssignmentAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleAssignment"


    // $ANTLR start "entryRuleValue"
    // InternalTyphonDL.g:528:1: entryRuleValue : ruleValue EOF ;
    public final void entryRuleValue() throws RecognitionException {
        try {
            // InternalTyphonDL.g:529:1: ( ruleValue EOF )
            // InternalTyphonDL.g:530:1: ruleValue EOF
            {
             before(grammarAccess.getValueRule()); 
            pushFollow(FOLLOW_1);
            ruleValue();

            state._fsp--;

             after(grammarAccess.getValueRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleValue"


    // $ANTLR start "ruleValue"
    // InternalTyphonDL.g:537:1: ruleValue : ( ( ( rule__Value__Alternatives ) ) ( ( rule__Value__Alternatives )* ) ) ;
    public final void ruleValue() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:541:2: ( ( ( ( rule__Value__Alternatives ) ) ( ( rule__Value__Alternatives )* ) ) )
            // InternalTyphonDL.g:542:2: ( ( ( rule__Value__Alternatives ) ) ( ( rule__Value__Alternatives )* ) )
            {
            // InternalTyphonDL.g:542:2: ( ( ( rule__Value__Alternatives ) ) ( ( rule__Value__Alternatives )* ) )
            // InternalTyphonDL.g:543:3: ( ( rule__Value__Alternatives ) ) ( ( rule__Value__Alternatives )* )
            {
            // InternalTyphonDL.g:543:3: ( ( rule__Value__Alternatives ) )
            // InternalTyphonDL.g:544:4: ( rule__Value__Alternatives )
            {
             before(grammarAccess.getValueAccess().getAlternatives()); 
            // InternalTyphonDL.g:545:4: ( rule__Value__Alternatives )
            // InternalTyphonDL.g:545:5: rule__Value__Alternatives
            {
            pushFollow(FOLLOW_4);
            rule__Value__Alternatives();

            state._fsp--;


            }

             after(grammarAccess.getValueAccess().getAlternatives()); 

            }

            // InternalTyphonDL.g:548:3: ( ( rule__Value__Alternatives )* )
            // InternalTyphonDL.g:549:4: ( rule__Value__Alternatives )*
            {
             before(grammarAccess.getValueAccess().getAlternatives()); 
            // InternalTyphonDL.g:550:4: ( rule__Value__Alternatives )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==RULE_ID) ) {
                    int LA2_2 = input.LA(2);

                    if ( (LA2_2==13) ) {
                        int LA2_4 = input.LA(3);

                        if ( (LA2_4==EOF||LA2_4==RULE_STRING||LA2_4==21||(LA2_4>=31 && LA2_4<=32)) ) {
                            alt2=1;
                        }
                        else if ( ((LA2_4>=RULE_ID && LA2_4<=RULE_INT)||(LA2_4>=12 && LA2_4<=15)||LA2_4==29) ) {
                            alt2=1;
                        }


                    }
                    else if ( (LA2_2==EOF||(LA2_2>=RULE_STRING && LA2_2<=RULE_INT)||LA2_2==12||(LA2_2>=14 && LA2_2<=15)||LA2_2==21||LA2_2==29||(LA2_2>=31 && LA2_2<=32)) ) {
                        alt2=1;
                    }


                }
                else if ( (LA2_0==RULE_STRING||LA2_0==RULE_INT||(LA2_0>=12 && LA2_0<=15)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // InternalTyphonDL.g:550:5: rule__Value__Alternatives
            	    {
            	    pushFollow(FOLLOW_4);
            	    rule__Value__Alternatives();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

             after(grammarAccess.getValueAccess().getAlternatives()); 

            }


            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleValue"


    // $ANTLR start "entryRuleFeature"
    // InternalTyphonDL.g:560:1: entryRuleFeature : ruleFeature EOF ;
    public final void entryRuleFeature() throws RecognitionException {
        try {
            // InternalTyphonDL.g:561:1: ( ruleFeature EOF )
            // InternalTyphonDL.g:562:1: ruleFeature EOF
            {
             before(grammarAccess.getFeatureRule()); 
            pushFollow(FOLLOW_1);
            ruleFeature();

            state._fsp--;

             after(grammarAccess.getFeatureRule()); 
            match(input,EOF,FOLLOW_2); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "entryRuleFeature"


    // $ANTLR start "ruleFeature"
    // InternalTyphonDL.g:569:1: ruleFeature : ( ( rule__Feature__Group__0 ) ) ;
    public final void ruleFeature() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:573:2: ( ( ( rule__Feature__Group__0 ) ) )
            // InternalTyphonDL.g:574:2: ( ( rule__Feature__Group__0 ) )
            {
            // InternalTyphonDL.g:574:2: ( ( rule__Feature__Group__0 ) )
            // InternalTyphonDL.g:575:3: ( rule__Feature__Group__0 )
            {
             before(grammarAccess.getFeatureAccess().getGroup()); 
            // InternalTyphonDL.g:576:3: ( rule__Feature__Group__0 )
            // InternalTyphonDL.g:576:4: rule__Feature__Group__0
            {
            pushFollow(FOLLOW_2);
            rule__Feature__Group__0();

            state._fsp--;


            }

             after(grammarAccess.getFeatureAccess().getGroup()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "ruleFeature"


    // $ANTLR start "rule__Type__Alternatives"
    // InternalTyphonDL.g:584:1: rule__Type__Alternatives : ( ( rulePlatformType ) | ( ruleDataType ) | ( ruleDeployment ) | ( ruleDBType ) | ( ruleEntity ) );
    public final void rule__Type__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:588:1: ( ( rulePlatformType ) | ( ruleDataType ) | ( ruleDeployment ) | ( ruleDBType ) | ( ruleEntity ) )
            int alt3=5;
            switch ( input.LA(1) ) {
            case 17:
                {
                alt3=1;
                }
                break;
            case 16:
                {
                alt3=2;
                }
                break;
            case 19:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
                {
                alt3=3;
                }
                break;
            case 18:
                {
                alt3=4;
                }
                break;
            case 27:
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
                    // InternalTyphonDL.g:589:2: ( rulePlatformType )
                    {
                    // InternalTyphonDL.g:589:2: ( rulePlatformType )
                    // InternalTyphonDL.g:590:3: rulePlatformType
                    {
                     before(grammarAccess.getTypeAccess().getPlatformTypeParserRuleCall_0()); 
                    pushFollow(FOLLOW_2);
                    rulePlatformType();

                    state._fsp--;

                     after(grammarAccess.getTypeAccess().getPlatformTypeParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalTyphonDL.g:595:2: ( ruleDataType )
                    {
                    // InternalTyphonDL.g:595:2: ( ruleDataType )
                    // InternalTyphonDL.g:596:3: ruleDataType
                    {
                     before(grammarAccess.getTypeAccess().getDataTypeParserRuleCall_1()); 
                    pushFollow(FOLLOW_2);
                    ruleDataType();

                    state._fsp--;

                     after(grammarAccess.getTypeAccess().getDataTypeParserRuleCall_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalTyphonDL.g:601:2: ( ruleDeployment )
                    {
                    // InternalTyphonDL.g:601:2: ( ruleDeployment )
                    // InternalTyphonDL.g:602:3: ruleDeployment
                    {
                     before(grammarAccess.getTypeAccess().getDeploymentParserRuleCall_2()); 
                    pushFollow(FOLLOW_2);
                    ruleDeployment();

                    state._fsp--;

                     after(grammarAccess.getTypeAccess().getDeploymentParserRuleCall_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalTyphonDL.g:607:2: ( ruleDBType )
                    {
                    // InternalTyphonDL.g:607:2: ( ruleDBType )
                    // InternalTyphonDL.g:608:3: ruleDBType
                    {
                     before(grammarAccess.getTypeAccess().getDBTypeParserRuleCall_3()); 
                    pushFollow(FOLLOW_2);
                    ruleDBType();

                    state._fsp--;

                     after(grammarAccess.getTypeAccess().getDBTypeParserRuleCall_3()); 

                    }


                    }
                    break;
                case 5 :
                    // InternalTyphonDL.g:613:2: ( ruleEntity )
                    {
                    // InternalTyphonDL.g:613:2: ( ruleEntity )
                    // InternalTyphonDL.g:614:3: ruleEntity
                    {
                     before(grammarAccess.getTypeAccess().getEntityParserRuleCall_4()); 
                    pushFollow(FOLLOW_2);
                    ruleEntity();

                    state._fsp--;

                     after(grammarAccess.getTypeAccess().getEntityParserRuleCall_4()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Type__Alternatives"


    // $ANTLR start "rule__Deployment__Alternatives"
    // InternalTyphonDL.g:623:1: rule__Deployment__Alternatives : ( ( rulePlatform ) | ( ruleCluster ) | ( ruleApplication ) | ( ruleContainer ) | ( ruleService ) );
    public final void rule__Deployment__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:627:1: ( ( rulePlatform ) | ( ruleCluster ) | ( ruleApplication ) | ( ruleContainer ) | ( ruleService ) )
            int alt4=5;
            switch ( input.LA(1) ) {
            case 19:
                {
                alt4=1;
                }
                break;
            case 22:
                {
                alt4=2;
                }
                break;
            case 23:
                {
                alt4=3;
                }
                break;
            case 24:
                {
                alt4=4;
                }
                break;
            case 25:
            case 26:
                {
                alt4=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // InternalTyphonDL.g:628:2: ( rulePlatform )
                    {
                    // InternalTyphonDL.g:628:2: ( rulePlatform )
                    // InternalTyphonDL.g:629:3: rulePlatform
                    {
                     before(grammarAccess.getDeploymentAccess().getPlatformParserRuleCall_0()); 
                    pushFollow(FOLLOW_2);
                    rulePlatform();

                    state._fsp--;

                     after(grammarAccess.getDeploymentAccess().getPlatformParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalTyphonDL.g:634:2: ( ruleCluster )
                    {
                    // InternalTyphonDL.g:634:2: ( ruleCluster )
                    // InternalTyphonDL.g:635:3: ruleCluster
                    {
                     before(grammarAccess.getDeploymentAccess().getClusterParserRuleCall_1()); 
                    pushFollow(FOLLOW_2);
                    ruleCluster();

                    state._fsp--;

                     after(grammarAccess.getDeploymentAccess().getClusterParserRuleCall_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalTyphonDL.g:640:2: ( ruleApplication )
                    {
                    // InternalTyphonDL.g:640:2: ( ruleApplication )
                    // InternalTyphonDL.g:641:3: ruleApplication
                    {
                     before(grammarAccess.getDeploymentAccess().getApplicationParserRuleCall_2()); 
                    pushFollow(FOLLOW_2);
                    ruleApplication();

                    state._fsp--;

                     after(grammarAccess.getDeploymentAccess().getApplicationParserRuleCall_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalTyphonDL.g:646:2: ( ruleContainer )
                    {
                    // InternalTyphonDL.g:646:2: ( ruleContainer )
                    // InternalTyphonDL.g:647:3: ruleContainer
                    {
                     before(grammarAccess.getDeploymentAccess().getContainerParserRuleCall_3()); 
                    pushFollow(FOLLOW_2);
                    ruleContainer();

                    state._fsp--;

                     after(grammarAccess.getDeploymentAccess().getContainerParserRuleCall_3()); 

                    }


                    }
                    break;
                case 5 :
                    // InternalTyphonDL.g:652:2: ( ruleService )
                    {
                    // InternalTyphonDL.g:652:2: ( ruleService )
                    // InternalTyphonDL.g:653:3: ruleService
                    {
                     before(grammarAccess.getDeploymentAccess().getServiceParserRuleCall_4()); 
                    pushFollow(FOLLOW_2);
                    ruleService();

                    state._fsp--;

                     after(grammarAccess.getDeploymentAccess().getServiceParserRuleCall_4()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Deployment__Alternatives"


    // $ANTLR start "rule__Container__Alternatives_4"
    // InternalTyphonDL.g:662:1: rule__Container__Alternatives_4 : ( ( ( rule__Container__PropertiesAssignment_4_0 ) ) | ( ( rule__Container__FeaturesAssignment_4_1 ) ) );
    public final void rule__Container__Alternatives_4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:666:1: ( ( ( rule__Container__PropertiesAssignment_4_0 ) ) | ( ( rule__Container__FeaturesAssignment_4_1 ) ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==RULE_ID) ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==20||LA5_1==30||LA5_1==33) ) {
                    alt5=1;
                }
                else if ( (LA5_1==13) ) {
                    alt5=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA5_0==29) ) {
                alt5=1;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // InternalTyphonDL.g:667:2: ( ( rule__Container__PropertiesAssignment_4_0 ) )
                    {
                    // InternalTyphonDL.g:667:2: ( ( rule__Container__PropertiesAssignment_4_0 ) )
                    // InternalTyphonDL.g:668:3: ( rule__Container__PropertiesAssignment_4_0 )
                    {
                     before(grammarAccess.getContainerAccess().getPropertiesAssignment_4_0()); 
                    // InternalTyphonDL.g:669:3: ( rule__Container__PropertiesAssignment_4_0 )
                    // InternalTyphonDL.g:669:4: rule__Container__PropertiesAssignment_4_0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Container__PropertiesAssignment_4_0();

                    state._fsp--;


                    }

                     after(grammarAccess.getContainerAccess().getPropertiesAssignment_4_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalTyphonDL.g:673:2: ( ( rule__Container__FeaturesAssignment_4_1 ) )
                    {
                    // InternalTyphonDL.g:673:2: ( ( rule__Container__FeaturesAssignment_4_1 ) )
                    // InternalTyphonDL.g:674:3: ( rule__Container__FeaturesAssignment_4_1 )
                    {
                     before(grammarAccess.getContainerAccess().getFeaturesAssignment_4_1()); 
                    // InternalTyphonDL.g:675:3: ( rule__Container__FeaturesAssignment_4_1 )
                    // InternalTyphonDL.g:675:4: rule__Container__FeaturesAssignment_4_1
                    {
                    pushFollow(FOLLOW_2);
                    rule__Container__FeaturesAssignment_4_1();

                    state._fsp--;


                    }

                     after(grammarAccess.getContainerAccess().getFeaturesAssignment_4_1()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__Alternatives_4"


    // $ANTLR start "rule__Service__Alternatives"
    // InternalTyphonDL.g:683:1: rule__Service__Alternatives : ( ( ruleDBService ) | ( ruleBusinessService ) );
    public final void rule__Service__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:687:1: ( ( ruleDBService ) | ( ruleBusinessService ) )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==25) ) {
                alt6=1;
            }
            else if ( (LA6_0==26) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // InternalTyphonDL.g:688:2: ( ruleDBService )
                    {
                    // InternalTyphonDL.g:688:2: ( ruleDBService )
                    // InternalTyphonDL.g:689:3: ruleDBService
                    {
                     before(grammarAccess.getServiceAccess().getDBServiceParserRuleCall_0()); 
                    pushFollow(FOLLOW_2);
                    ruleDBService();

                    state._fsp--;

                     after(grammarAccess.getServiceAccess().getDBServiceParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalTyphonDL.g:694:2: ( ruleBusinessService )
                    {
                    // InternalTyphonDL.g:694:2: ( ruleBusinessService )
                    // InternalTyphonDL.g:695:3: ruleBusinessService
                    {
                     before(grammarAccess.getServiceAccess().getBusinessServiceParserRuleCall_1()); 
                    pushFollow(FOLLOW_2);
                    ruleBusinessService();

                    state._fsp--;

                     after(grammarAccess.getServiceAccess().getBusinessServiceParserRuleCall_1()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Service__Alternatives"


    // $ANTLR start "rule__Property__Alternatives"
    // InternalTyphonDL.g:704:1: rule__Property__Alternatives : ( ( ruleAssignment ) | ( ruleAssignmentList ) | ( ruleCommaSeparatedAssignmentList ) | ( ruleEnvList ) );
    public final void rule__Property__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:708:1: ( ( ruleAssignment ) | ( ruleAssignmentList ) | ( ruleCommaSeparatedAssignmentList ) | ( ruleEnvList ) )
            int alt7=4;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==RULE_ID) ) {
                switch ( input.LA(2) ) {
                case 30:
                    {
                    alt7=3;
                    }
                    break;
                case 20:
                    {
                    alt7=2;
                    }
                    break;
                case 33:
                    {
                    alt7=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA7_0==29) ) {
                alt7=4;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // InternalTyphonDL.g:709:2: ( ruleAssignment )
                    {
                    // InternalTyphonDL.g:709:2: ( ruleAssignment )
                    // InternalTyphonDL.g:710:3: ruleAssignment
                    {
                     before(grammarAccess.getPropertyAccess().getAssignmentParserRuleCall_0()); 
                    pushFollow(FOLLOW_2);
                    ruleAssignment();

                    state._fsp--;

                     after(grammarAccess.getPropertyAccess().getAssignmentParserRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalTyphonDL.g:715:2: ( ruleAssignmentList )
                    {
                    // InternalTyphonDL.g:715:2: ( ruleAssignmentList )
                    // InternalTyphonDL.g:716:3: ruleAssignmentList
                    {
                     before(grammarAccess.getPropertyAccess().getAssignmentListParserRuleCall_1()); 
                    pushFollow(FOLLOW_2);
                    ruleAssignmentList();

                    state._fsp--;

                     after(grammarAccess.getPropertyAccess().getAssignmentListParserRuleCall_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalTyphonDL.g:721:2: ( ruleCommaSeparatedAssignmentList )
                    {
                    // InternalTyphonDL.g:721:2: ( ruleCommaSeparatedAssignmentList )
                    // InternalTyphonDL.g:722:3: ruleCommaSeparatedAssignmentList
                    {
                     before(grammarAccess.getPropertyAccess().getCommaSeparatedAssignmentListParserRuleCall_2()); 
                    pushFollow(FOLLOW_2);
                    ruleCommaSeparatedAssignmentList();

                    state._fsp--;

                     after(grammarAccess.getPropertyAccess().getCommaSeparatedAssignmentListParserRuleCall_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalTyphonDL.g:727:2: ( ruleEnvList )
                    {
                    // InternalTyphonDL.g:727:2: ( ruleEnvList )
                    // InternalTyphonDL.g:728:3: ruleEnvList
                    {
                     before(grammarAccess.getPropertyAccess().getEnvListParserRuleCall_3()); 
                    pushFollow(FOLLOW_2);
                    ruleEnvList();

                    state._fsp--;

                     after(grammarAccess.getPropertyAccess().getEnvListParserRuleCall_3()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Property__Alternatives"


    // $ANTLR start "rule__Value__Alternatives"
    // InternalTyphonDL.g:737:1: rule__Value__Alternatives : ( ( RULE_STRING ) | ( RULE_ID ) | ( RULE_INT ) | ( '/' ) | ( ':' ) | ( '-' ) | ( '.' ) );
    public final void rule__Value__Alternatives() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:741:1: ( ( RULE_STRING ) | ( RULE_ID ) | ( RULE_INT ) | ( '/' ) | ( ':' ) | ( '-' ) | ( '.' ) )
            int alt8=7;
            switch ( input.LA(1) ) {
            case RULE_STRING:
                {
                alt8=1;
                }
                break;
            case RULE_ID:
                {
                alt8=2;
                }
                break;
            case RULE_INT:
                {
                alt8=3;
                }
                break;
            case 12:
                {
                alt8=4;
                }
                break;
            case 13:
                {
                alt8=5;
                }
                break;
            case 14:
                {
                alt8=6;
                }
                break;
            case 15:
                {
                alt8=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // InternalTyphonDL.g:742:2: ( RULE_STRING )
                    {
                    // InternalTyphonDL.g:742:2: ( RULE_STRING )
                    // InternalTyphonDL.g:743:3: RULE_STRING
                    {
                     before(grammarAccess.getValueAccess().getSTRINGTerminalRuleCall_0()); 
                    match(input,RULE_STRING,FOLLOW_2); 
                     after(grammarAccess.getValueAccess().getSTRINGTerminalRuleCall_0()); 

                    }


                    }
                    break;
                case 2 :
                    // InternalTyphonDL.g:748:2: ( RULE_ID )
                    {
                    // InternalTyphonDL.g:748:2: ( RULE_ID )
                    // InternalTyphonDL.g:749:3: RULE_ID
                    {
                     before(grammarAccess.getValueAccess().getIDTerminalRuleCall_1()); 
                    match(input,RULE_ID,FOLLOW_2); 
                     after(grammarAccess.getValueAccess().getIDTerminalRuleCall_1()); 

                    }


                    }
                    break;
                case 3 :
                    // InternalTyphonDL.g:754:2: ( RULE_INT )
                    {
                    // InternalTyphonDL.g:754:2: ( RULE_INT )
                    // InternalTyphonDL.g:755:3: RULE_INT
                    {
                     before(grammarAccess.getValueAccess().getINTTerminalRuleCall_2()); 
                    match(input,RULE_INT,FOLLOW_2); 
                     after(grammarAccess.getValueAccess().getINTTerminalRuleCall_2()); 

                    }


                    }
                    break;
                case 4 :
                    // InternalTyphonDL.g:760:2: ( '/' )
                    {
                    // InternalTyphonDL.g:760:2: ( '/' )
                    // InternalTyphonDL.g:761:3: '/'
                    {
                     before(grammarAccess.getValueAccess().getSolidusKeyword_3()); 
                    match(input,12,FOLLOW_2); 
                     after(grammarAccess.getValueAccess().getSolidusKeyword_3()); 

                    }


                    }
                    break;
                case 5 :
                    // InternalTyphonDL.g:766:2: ( ':' )
                    {
                    // InternalTyphonDL.g:766:2: ( ':' )
                    // InternalTyphonDL.g:767:3: ':'
                    {
                     before(grammarAccess.getValueAccess().getColonKeyword_4()); 
                    match(input,13,FOLLOW_2); 
                     after(grammarAccess.getValueAccess().getColonKeyword_4()); 

                    }


                    }
                    break;
                case 6 :
                    // InternalTyphonDL.g:772:2: ( '-' )
                    {
                    // InternalTyphonDL.g:772:2: ( '-' )
                    // InternalTyphonDL.g:773:3: '-'
                    {
                     before(grammarAccess.getValueAccess().getHyphenMinusKeyword_5()); 
                    match(input,14,FOLLOW_2); 
                     after(grammarAccess.getValueAccess().getHyphenMinusKeyword_5()); 

                    }


                    }
                    break;
                case 7 :
                    // InternalTyphonDL.g:778:2: ( '.' )
                    {
                    // InternalTyphonDL.g:778:2: ( '.' )
                    // InternalTyphonDL.g:779:3: '.'
                    {
                     before(grammarAccess.getValueAccess().getFullStopKeyword_6()); 
                    match(input,15,FOLLOW_2); 
                     after(grammarAccess.getValueAccess().getFullStopKeyword_6()); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Value__Alternatives"


    // $ANTLR start "rule__DataType__Group__0"
    // InternalTyphonDL.g:788:1: rule__DataType__Group__0 : rule__DataType__Group__0__Impl rule__DataType__Group__1 ;
    public final void rule__DataType__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:792:1: ( rule__DataType__Group__0__Impl rule__DataType__Group__1 )
            // InternalTyphonDL.g:793:2: rule__DataType__Group__0__Impl rule__DataType__Group__1
            {
            pushFollow(FOLLOW_5);
            rule__DataType__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__DataType__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DataType__Group__0"


    // $ANTLR start "rule__DataType__Group__0__Impl"
    // InternalTyphonDL.g:800:1: rule__DataType__Group__0__Impl : ( 'datatype' ) ;
    public final void rule__DataType__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:804:1: ( ( 'datatype' ) )
            // InternalTyphonDL.g:805:1: ( 'datatype' )
            {
            // InternalTyphonDL.g:805:1: ( 'datatype' )
            // InternalTyphonDL.g:806:2: 'datatype'
            {
             before(grammarAccess.getDataTypeAccess().getDatatypeKeyword_0()); 
            match(input,16,FOLLOW_2); 
             after(grammarAccess.getDataTypeAccess().getDatatypeKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DataType__Group__0__Impl"


    // $ANTLR start "rule__DataType__Group__1"
    // InternalTyphonDL.g:815:1: rule__DataType__Group__1 : rule__DataType__Group__1__Impl ;
    public final void rule__DataType__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:819:1: ( rule__DataType__Group__1__Impl )
            // InternalTyphonDL.g:820:2: rule__DataType__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__DataType__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DataType__Group__1"


    // $ANTLR start "rule__DataType__Group__1__Impl"
    // InternalTyphonDL.g:826:1: rule__DataType__Group__1__Impl : ( ( rule__DataType__NameAssignment_1 ) ) ;
    public final void rule__DataType__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:830:1: ( ( ( rule__DataType__NameAssignment_1 ) ) )
            // InternalTyphonDL.g:831:1: ( ( rule__DataType__NameAssignment_1 ) )
            {
            // InternalTyphonDL.g:831:1: ( ( rule__DataType__NameAssignment_1 ) )
            // InternalTyphonDL.g:832:2: ( rule__DataType__NameAssignment_1 )
            {
             before(grammarAccess.getDataTypeAccess().getNameAssignment_1()); 
            // InternalTyphonDL.g:833:2: ( rule__DataType__NameAssignment_1 )
            // InternalTyphonDL.g:833:3: rule__DataType__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__DataType__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getDataTypeAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DataType__Group__1__Impl"


    // $ANTLR start "rule__PlatformType__Group__0"
    // InternalTyphonDL.g:842:1: rule__PlatformType__Group__0 : rule__PlatformType__Group__0__Impl rule__PlatformType__Group__1 ;
    public final void rule__PlatformType__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:846:1: ( rule__PlatformType__Group__0__Impl rule__PlatformType__Group__1 )
            // InternalTyphonDL.g:847:2: rule__PlatformType__Group__0__Impl rule__PlatformType__Group__1
            {
            pushFollow(FOLLOW_5);
            rule__PlatformType__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__PlatformType__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlatformType__Group__0"


    // $ANTLR start "rule__PlatformType__Group__0__Impl"
    // InternalTyphonDL.g:854:1: rule__PlatformType__Group__0__Impl : ( 'platformtype' ) ;
    public final void rule__PlatformType__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:858:1: ( ( 'platformtype' ) )
            // InternalTyphonDL.g:859:1: ( 'platformtype' )
            {
            // InternalTyphonDL.g:859:1: ( 'platformtype' )
            // InternalTyphonDL.g:860:2: 'platformtype'
            {
             before(grammarAccess.getPlatformTypeAccess().getPlatformtypeKeyword_0()); 
            match(input,17,FOLLOW_2); 
             after(grammarAccess.getPlatformTypeAccess().getPlatformtypeKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlatformType__Group__0__Impl"


    // $ANTLR start "rule__PlatformType__Group__1"
    // InternalTyphonDL.g:869:1: rule__PlatformType__Group__1 : rule__PlatformType__Group__1__Impl ;
    public final void rule__PlatformType__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:873:1: ( rule__PlatformType__Group__1__Impl )
            // InternalTyphonDL.g:874:2: rule__PlatformType__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__PlatformType__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlatformType__Group__1"


    // $ANTLR start "rule__PlatformType__Group__1__Impl"
    // InternalTyphonDL.g:880:1: rule__PlatformType__Group__1__Impl : ( ( rule__PlatformType__NameAssignment_1 ) ) ;
    public final void rule__PlatformType__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:884:1: ( ( ( rule__PlatformType__NameAssignment_1 ) ) )
            // InternalTyphonDL.g:885:1: ( ( rule__PlatformType__NameAssignment_1 ) )
            {
            // InternalTyphonDL.g:885:1: ( ( rule__PlatformType__NameAssignment_1 ) )
            // InternalTyphonDL.g:886:2: ( rule__PlatformType__NameAssignment_1 )
            {
             before(grammarAccess.getPlatformTypeAccess().getNameAssignment_1()); 
            // InternalTyphonDL.g:887:2: ( rule__PlatformType__NameAssignment_1 )
            // InternalTyphonDL.g:887:3: rule__PlatformType__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__PlatformType__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getPlatformTypeAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlatformType__Group__1__Impl"


    // $ANTLR start "rule__DBType__Group__0"
    // InternalTyphonDL.g:896:1: rule__DBType__Group__0 : rule__DBType__Group__0__Impl rule__DBType__Group__1 ;
    public final void rule__DBType__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:900:1: ( rule__DBType__Group__0__Impl rule__DBType__Group__1 )
            // InternalTyphonDL.g:901:2: rule__DBType__Group__0__Impl rule__DBType__Group__1
            {
            pushFollow(FOLLOW_5);
            rule__DBType__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__DBType__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBType__Group__0"


    // $ANTLR start "rule__DBType__Group__0__Impl"
    // InternalTyphonDL.g:908:1: rule__DBType__Group__0__Impl : ( 'dbtype' ) ;
    public final void rule__DBType__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:912:1: ( ( 'dbtype' ) )
            // InternalTyphonDL.g:913:1: ( 'dbtype' )
            {
            // InternalTyphonDL.g:913:1: ( 'dbtype' )
            // InternalTyphonDL.g:914:2: 'dbtype'
            {
             before(grammarAccess.getDBTypeAccess().getDbtypeKeyword_0()); 
            match(input,18,FOLLOW_2); 
             after(grammarAccess.getDBTypeAccess().getDbtypeKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBType__Group__0__Impl"


    // $ANTLR start "rule__DBType__Group__1"
    // InternalTyphonDL.g:923:1: rule__DBType__Group__1 : rule__DBType__Group__1__Impl ;
    public final void rule__DBType__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:927:1: ( rule__DBType__Group__1__Impl )
            // InternalTyphonDL.g:928:2: rule__DBType__Group__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__DBType__Group__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBType__Group__1"


    // $ANTLR start "rule__DBType__Group__1__Impl"
    // InternalTyphonDL.g:934:1: rule__DBType__Group__1__Impl : ( ( rule__DBType__NameAssignment_1 ) ) ;
    public final void rule__DBType__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:938:1: ( ( ( rule__DBType__NameAssignment_1 ) ) )
            // InternalTyphonDL.g:939:1: ( ( rule__DBType__NameAssignment_1 ) )
            {
            // InternalTyphonDL.g:939:1: ( ( rule__DBType__NameAssignment_1 ) )
            // InternalTyphonDL.g:940:2: ( rule__DBType__NameAssignment_1 )
            {
             before(grammarAccess.getDBTypeAccess().getNameAssignment_1()); 
            // InternalTyphonDL.g:941:2: ( rule__DBType__NameAssignment_1 )
            // InternalTyphonDL.g:941:3: rule__DBType__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__DBType__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getDBTypeAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBType__Group__1__Impl"


    // $ANTLR start "rule__Platform__Group__0"
    // InternalTyphonDL.g:950:1: rule__Platform__Group__0 : rule__Platform__Group__0__Impl rule__Platform__Group__1 ;
    public final void rule__Platform__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:954:1: ( rule__Platform__Group__0__Impl rule__Platform__Group__1 )
            // InternalTyphonDL.g:955:2: rule__Platform__Group__0__Impl rule__Platform__Group__1
            {
            pushFollow(FOLLOW_6);
            rule__Platform__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Platform__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__0"


    // $ANTLR start "rule__Platform__Group__0__Impl"
    // InternalTyphonDL.g:962:1: rule__Platform__Group__0__Impl : ( () ) ;
    public final void rule__Platform__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:966:1: ( ( () ) )
            // InternalTyphonDL.g:967:1: ( () )
            {
            // InternalTyphonDL.g:967:1: ( () )
            // InternalTyphonDL.g:968:2: ()
            {
             before(grammarAccess.getPlatformAccess().getPlatformAction_0()); 
            // InternalTyphonDL.g:969:2: ()
            // InternalTyphonDL.g:969:3: 
            {
            }

             after(grammarAccess.getPlatformAccess().getPlatformAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__0__Impl"


    // $ANTLR start "rule__Platform__Group__1"
    // InternalTyphonDL.g:977:1: rule__Platform__Group__1 : rule__Platform__Group__1__Impl rule__Platform__Group__2 ;
    public final void rule__Platform__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:981:1: ( rule__Platform__Group__1__Impl rule__Platform__Group__2 )
            // InternalTyphonDL.g:982:2: rule__Platform__Group__1__Impl rule__Platform__Group__2
            {
            pushFollow(FOLLOW_5);
            rule__Platform__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Platform__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__1"


    // $ANTLR start "rule__Platform__Group__1__Impl"
    // InternalTyphonDL.g:989:1: rule__Platform__Group__1__Impl : ( 'platform' ) ;
    public final void rule__Platform__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:993:1: ( ( 'platform' ) )
            // InternalTyphonDL.g:994:1: ( 'platform' )
            {
            // InternalTyphonDL.g:994:1: ( 'platform' )
            // InternalTyphonDL.g:995:2: 'platform'
            {
             before(grammarAccess.getPlatformAccess().getPlatformKeyword_1()); 
            match(input,19,FOLLOW_2); 
             after(grammarAccess.getPlatformAccess().getPlatformKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__1__Impl"


    // $ANTLR start "rule__Platform__Group__2"
    // InternalTyphonDL.g:1004:1: rule__Platform__Group__2 : rule__Platform__Group__2__Impl rule__Platform__Group__3 ;
    public final void rule__Platform__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1008:1: ( rule__Platform__Group__2__Impl rule__Platform__Group__3 )
            // InternalTyphonDL.g:1009:2: rule__Platform__Group__2__Impl rule__Platform__Group__3
            {
            pushFollow(FOLLOW_7);
            rule__Platform__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Platform__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__2"


    // $ANTLR start "rule__Platform__Group__2__Impl"
    // InternalTyphonDL.g:1016:1: rule__Platform__Group__2__Impl : ( ( rule__Platform__NameAssignment_2 ) ) ;
    public final void rule__Platform__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1020:1: ( ( ( rule__Platform__NameAssignment_2 ) ) )
            // InternalTyphonDL.g:1021:1: ( ( rule__Platform__NameAssignment_2 ) )
            {
            // InternalTyphonDL.g:1021:1: ( ( rule__Platform__NameAssignment_2 ) )
            // InternalTyphonDL.g:1022:2: ( rule__Platform__NameAssignment_2 )
            {
             before(grammarAccess.getPlatformAccess().getNameAssignment_2()); 
            // InternalTyphonDL.g:1023:2: ( rule__Platform__NameAssignment_2 )
            // InternalTyphonDL.g:1023:3: rule__Platform__NameAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__Platform__NameAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getPlatformAccess().getNameAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__2__Impl"


    // $ANTLR start "rule__Platform__Group__3"
    // InternalTyphonDL.g:1031:1: rule__Platform__Group__3 : rule__Platform__Group__3__Impl rule__Platform__Group__4 ;
    public final void rule__Platform__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1035:1: ( rule__Platform__Group__3__Impl rule__Platform__Group__4 )
            // InternalTyphonDL.g:1036:2: rule__Platform__Group__3__Impl rule__Platform__Group__4
            {
            pushFollow(FOLLOW_5);
            rule__Platform__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Platform__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__3"


    // $ANTLR start "rule__Platform__Group__3__Impl"
    // InternalTyphonDL.g:1043:1: rule__Platform__Group__3__Impl : ( ':' ) ;
    public final void rule__Platform__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1047:1: ( ( ':' ) )
            // InternalTyphonDL.g:1048:1: ( ':' )
            {
            // InternalTyphonDL.g:1048:1: ( ':' )
            // InternalTyphonDL.g:1049:2: ':'
            {
             before(grammarAccess.getPlatformAccess().getColonKeyword_3()); 
            match(input,13,FOLLOW_2); 
             after(grammarAccess.getPlatformAccess().getColonKeyword_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__3__Impl"


    // $ANTLR start "rule__Platform__Group__4"
    // InternalTyphonDL.g:1058:1: rule__Platform__Group__4 : rule__Platform__Group__4__Impl rule__Platform__Group__5 ;
    public final void rule__Platform__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1062:1: ( rule__Platform__Group__4__Impl rule__Platform__Group__5 )
            // InternalTyphonDL.g:1063:2: rule__Platform__Group__4__Impl rule__Platform__Group__5
            {
            pushFollow(FOLLOW_8);
            rule__Platform__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Platform__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__4"


    // $ANTLR start "rule__Platform__Group__4__Impl"
    // InternalTyphonDL.g:1070:1: rule__Platform__Group__4__Impl : ( ( rule__Platform__TypeAssignment_4 ) ) ;
    public final void rule__Platform__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1074:1: ( ( ( rule__Platform__TypeAssignment_4 ) ) )
            // InternalTyphonDL.g:1075:1: ( ( rule__Platform__TypeAssignment_4 ) )
            {
            // InternalTyphonDL.g:1075:1: ( ( rule__Platform__TypeAssignment_4 ) )
            // InternalTyphonDL.g:1076:2: ( rule__Platform__TypeAssignment_4 )
            {
             before(grammarAccess.getPlatformAccess().getTypeAssignment_4()); 
            // InternalTyphonDL.g:1077:2: ( rule__Platform__TypeAssignment_4 )
            // InternalTyphonDL.g:1077:3: rule__Platform__TypeAssignment_4
            {
            pushFollow(FOLLOW_2);
            rule__Platform__TypeAssignment_4();

            state._fsp--;


            }

             after(grammarAccess.getPlatformAccess().getTypeAssignment_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__4__Impl"


    // $ANTLR start "rule__Platform__Group__5"
    // InternalTyphonDL.g:1085:1: rule__Platform__Group__5 : rule__Platform__Group__5__Impl rule__Platform__Group__6 ;
    public final void rule__Platform__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1089:1: ( rule__Platform__Group__5__Impl rule__Platform__Group__6 )
            // InternalTyphonDL.g:1090:2: rule__Platform__Group__5__Impl rule__Platform__Group__6
            {
            pushFollow(FOLLOW_9);
            rule__Platform__Group__5__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Platform__Group__6();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__5"


    // $ANTLR start "rule__Platform__Group__5__Impl"
    // InternalTyphonDL.g:1097:1: rule__Platform__Group__5__Impl : ( '{' ) ;
    public final void rule__Platform__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1101:1: ( ( '{' ) )
            // InternalTyphonDL.g:1102:1: ( '{' )
            {
            // InternalTyphonDL.g:1102:1: ( '{' )
            // InternalTyphonDL.g:1103:2: '{'
            {
             before(grammarAccess.getPlatformAccess().getLeftCurlyBracketKeyword_5()); 
            match(input,20,FOLLOW_2); 
             after(grammarAccess.getPlatformAccess().getLeftCurlyBracketKeyword_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__5__Impl"


    // $ANTLR start "rule__Platform__Group__6"
    // InternalTyphonDL.g:1112:1: rule__Platform__Group__6 : rule__Platform__Group__6__Impl rule__Platform__Group__7 ;
    public final void rule__Platform__Group__6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1116:1: ( rule__Platform__Group__6__Impl rule__Platform__Group__7 )
            // InternalTyphonDL.g:1117:2: rule__Platform__Group__6__Impl rule__Platform__Group__7
            {
            pushFollow(FOLLOW_9);
            rule__Platform__Group__6__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Platform__Group__7();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__6"


    // $ANTLR start "rule__Platform__Group__6__Impl"
    // InternalTyphonDL.g:1124:1: rule__Platform__Group__6__Impl : ( ( rule__Platform__ClustersAssignment_6 )* ) ;
    public final void rule__Platform__Group__6__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1128:1: ( ( ( rule__Platform__ClustersAssignment_6 )* ) )
            // InternalTyphonDL.g:1129:1: ( ( rule__Platform__ClustersAssignment_6 )* )
            {
            // InternalTyphonDL.g:1129:1: ( ( rule__Platform__ClustersAssignment_6 )* )
            // InternalTyphonDL.g:1130:2: ( rule__Platform__ClustersAssignment_6 )*
            {
             before(grammarAccess.getPlatformAccess().getClustersAssignment_6()); 
            // InternalTyphonDL.g:1131:2: ( rule__Platform__ClustersAssignment_6 )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==22) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalTyphonDL.g:1131:3: rule__Platform__ClustersAssignment_6
            	    {
            	    pushFollow(FOLLOW_10);
            	    rule__Platform__ClustersAssignment_6();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

             after(grammarAccess.getPlatformAccess().getClustersAssignment_6()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__6__Impl"


    // $ANTLR start "rule__Platform__Group__7"
    // InternalTyphonDL.g:1139:1: rule__Platform__Group__7 : rule__Platform__Group__7__Impl ;
    public final void rule__Platform__Group__7() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1143:1: ( rule__Platform__Group__7__Impl )
            // InternalTyphonDL.g:1144:2: rule__Platform__Group__7__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Platform__Group__7__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__7"


    // $ANTLR start "rule__Platform__Group__7__Impl"
    // InternalTyphonDL.g:1150:1: rule__Platform__Group__7__Impl : ( '}' ) ;
    public final void rule__Platform__Group__7__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1154:1: ( ( '}' ) )
            // InternalTyphonDL.g:1155:1: ( '}' )
            {
            // InternalTyphonDL.g:1155:1: ( '}' )
            // InternalTyphonDL.g:1156:2: '}'
            {
             before(grammarAccess.getPlatformAccess().getRightCurlyBracketKeyword_7()); 
            match(input,21,FOLLOW_2); 
             after(grammarAccess.getPlatformAccess().getRightCurlyBracketKeyword_7()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__Group__7__Impl"


    // $ANTLR start "rule__Cluster__Group__0"
    // InternalTyphonDL.g:1166:1: rule__Cluster__Group__0 : rule__Cluster__Group__0__Impl rule__Cluster__Group__1 ;
    public final void rule__Cluster__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1170:1: ( rule__Cluster__Group__0__Impl rule__Cluster__Group__1 )
            // InternalTyphonDL.g:1171:2: rule__Cluster__Group__0__Impl rule__Cluster__Group__1
            {
            pushFollow(FOLLOW_11);
            rule__Cluster__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Cluster__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__Group__0"


    // $ANTLR start "rule__Cluster__Group__0__Impl"
    // InternalTyphonDL.g:1178:1: rule__Cluster__Group__0__Impl : ( () ) ;
    public final void rule__Cluster__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1182:1: ( ( () ) )
            // InternalTyphonDL.g:1183:1: ( () )
            {
            // InternalTyphonDL.g:1183:1: ( () )
            // InternalTyphonDL.g:1184:2: ()
            {
             before(grammarAccess.getClusterAccess().getClusterAction_0()); 
            // InternalTyphonDL.g:1185:2: ()
            // InternalTyphonDL.g:1185:3: 
            {
            }

             after(grammarAccess.getClusterAccess().getClusterAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__Group__0__Impl"


    // $ANTLR start "rule__Cluster__Group__1"
    // InternalTyphonDL.g:1193:1: rule__Cluster__Group__1 : rule__Cluster__Group__1__Impl rule__Cluster__Group__2 ;
    public final void rule__Cluster__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1197:1: ( rule__Cluster__Group__1__Impl rule__Cluster__Group__2 )
            // InternalTyphonDL.g:1198:2: rule__Cluster__Group__1__Impl rule__Cluster__Group__2
            {
            pushFollow(FOLLOW_5);
            rule__Cluster__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Cluster__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__Group__1"


    // $ANTLR start "rule__Cluster__Group__1__Impl"
    // InternalTyphonDL.g:1205:1: rule__Cluster__Group__1__Impl : ( 'cluster' ) ;
    public final void rule__Cluster__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1209:1: ( ( 'cluster' ) )
            // InternalTyphonDL.g:1210:1: ( 'cluster' )
            {
            // InternalTyphonDL.g:1210:1: ( 'cluster' )
            // InternalTyphonDL.g:1211:2: 'cluster'
            {
             before(grammarAccess.getClusterAccess().getClusterKeyword_1()); 
            match(input,22,FOLLOW_2); 
             after(grammarAccess.getClusterAccess().getClusterKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__Group__1__Impl"


    // $ANTLR start "rule__Cluster__Group__2"
    // InternalTyphonDL.g:1220:1: rule__Cluster__Group__2 : rule__Cluster__Group__2__Impl rule__Cluster__Group__3 ;
    public final void rule__Cluster__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1224:1: ( rule__Cluster__Group__2__Impl rule__Cluster__Group__3 )
            // InternalTyphonDL.g:1225:2: rule__Cluster__Group__2__Impl rule__Cluster__Group__3
            {
            pushFollow(FOLLOW_8);
            rule__Cluster__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Cluster__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__Group__2"


    // $ANTLR start "rule__Cluster__Group__2__Impl"
    // InternalTyphonDL.g:1232:1: rule__Cluster__Group__2__Impl : ( ( rule__Cluster__NameAssignment_2 ) ) ;
    public final void rule__Cluster__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1236:1: ( ( ( rule__Cluster__NameAssignment_2 ) ) )
            // InternalTyphonDL.g:1237:1: ( ( rule__Cluster__NameAssignment_2 ) )
            {
            // InternalTyphonDL.g:1237:1: ( ( rule__Cluster__NameAssignment_2 ) )
            // InternalTyphonDL.g:1238:2: ( rule__Cluster__NameAssignment_2 )
            {
             before(grammarAccess.getClusterAccess().getNameAssignment_2()); 
            // InternalTyphonDL.g:1239:2: ( rule__Cluster__NameAssignment_2 )
            // InternalTyphonDL.g:1239:3: rule__Cluster__NameAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__Cluster__NameAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getClusterAccess().getNameAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__Group__2__Impl"


    // $ANTLR start "rule__Cluster__Group__3"
    // InternalTyphonDL.g:1247:1: rule__Cluster__Group__3 : rule__Cluster__Group__3__Impl rule__Cluster__Group__4 ;
    public final void rule__Cluster__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1251:1: ( rule__Cluster__Group__3__Impl rule__Cluster__Group__4 )
            // InternalTyphonDL.g:1252:2: rule__Cluster__Group__3__Impl rule__Cluster__Group__4
            {
            pushFollow(FOLLOW_12);
            rule__Cluster__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Cluster__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__Group__3"


    // $ANTLR start "rule__Cluster__Group__3__Impl"
    // InternalTyphonDL.g:1259:1: rule__Cluster__Group__3__Impl : ( '{' ) ;
    public final void rule__Cluster__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1263:1: ( ( '{' ) )
            // InternalTyphonDL.g:1264:1: ( '{' )
            {
            // InternalTyphonDL.g:1264:1: ( '{' )
            // InternalTyphonDL.g:1265:2: '{'
            {
             before(grammarAccess.getClusterAccess().getLeftCurlyBracketKeyword_3()); 
            match(input,20,FOLLOW_2); 
             after(grammarAccess.getClusterAccess().getLeftCurlyBracketKeyword_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__Group__3__Impl"


    // $ANTLR start "rule__Cluster__Group__4"
    // InternalTyphonDL.g:1274:1: rule__Cluster__Group__4 : rule__Cluster__Group__4__Impl rule__Cluster__Group__5 ;
    public final void rule__Cluster__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1278:1: ( rule__Cluster__Group__4__Impl rule__Cluster__Group__5 )
            // InternalTyphonDL.g:1279:2: rule__Cluster__Group__4__Impl rule__Cluster__Group__5
            {
            pushFollow(FOLLOW_12);
            rule__Cluster__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Cluster__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__Group__4"


    // $ANTLR start "rule__Cluster__Group__4__Impl"
    // InternalTyphonDL.g:1286:1: rule__Cluster__Group__4__Impl : ( ( rule__Cluster__ApplicationsAssignment_4 )* ) ;
    public final void rule__Cluster__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1290:1: ( ( ( rule__Cluster__ApplicationsAssignment_4 )* ) )
            // InternalTyphonDL.g:1291:1: ( ( rule__Cluster__ApplicationsAssignment_4 )* )
            {
            // InternalTyphonDL.g:1291:1: ( ( rule__Cluster__ApplicationsAssignment_4 )* )
            // InternalTyphonDL.g:1292:2: ( rule__Cluster__ApplicationsAssignment_4 )*
            {
             before(grammarAccess.getClusterAccess().getApplicationsAssignment_4()); 
            // InternalTyphonDL.g:1293:2: ( rule__Cluster__ApplicationsAssignment_4 )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==23) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // InternalTyphonDL.g:1293:3: rule__Cluster__ApplicationsAssignment_4
            	    {
            	    pushFollow(FOLLOW_13);
            	    rule__Cluster__ApplicationsAssignment_4();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

             after(grammarAccess.getClusterAccess().getApplicationsAssignment_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__Group__4__Impl"


    // $ANTLR start "rule__Cluster__Group__5"
    // InternalTyphonDL.g:1301:1: rule__Cluster__Group__5 : rule__Cluster__Group__5__Impl ;
    public final void rule__Cluster__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1305:1: ( rule__Cluster__Group__5__Impl )
            // InternalTyphonDL.g:1306:2: rule__Cluster__Group__5__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Cluster__Group__5__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__Group__5"


    // $ANTLR start "rule__Cluster__Group__5__Impl"
    // InternalTyphonDL.g:1312:1: rule__Cluster__Group__5__Impl : ( '}' ) ;
    public final void rule__Cluster__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1316:1: ( ( '}' ) )
            // InternalTyphonDL.g:1317:1: ( '}' )
            {
            // InternalTyphonDL.g:1317:1: ( '}' )
            // InternalTyphonDL.g:1318:2: '}'
            {
             before(grammarAccess.getClusterAccess().getRightCurlyBracketKeyword_5()); 
            match(input,21,FOLLOW_2); 
             after(grammarAccess.getClusterAccess().getRightCurlyBracketKeyword_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__Group__5__Impl"


    // $ANTLR start "rule__Application__Group__0"
    // InternalTyphonDL.g:1328:1: rule__Application__Group__0 : rule__Application__Group__0__Impl rule__Application__Group__1 ;
    public final void rule__Application__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1332:1: ( rule__Application__Group__0__Impl rule__Application__Group__1 )
            // InternalTyphonDL.g:1333:2: rule__Application__Group__0__Impl rule__Application__Group__1
            {
            pushFollow(FOLLOW_14);
            rule__Application__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Application__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__Group__0"


    // $ANTLR start "rule__Application__Group__0__Impl"
    // InternalTyphonDL.g:1340:1: rule__Application__Group__0__Impl : ( () ) ;
    public final void rule__Application__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1344:1: ( ( () ) )
            // InternalTyphonDL.g:1345:1: ( () )
            {
            // InternalTyphonDL.g:1345:1: ( () )
            // InternalTyphonDL.g:1346:2: ()
            {
             before(grammarAccess.getApplicationAccess().getApplicationAction_0()); 
            // InternalTyphonDL.g:1347:2: ()
            // InternalTyphonDL.g:1347:3: 
            {
            }

             after(grammarAccess.getApplicationAccess().getApplicationAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__Group__0__Impl"


    // $ANTLR start "rule__Application__Group__1"
    // InternalTyphonDL.g:1355:1: rule__Application__Group__1 : rule__Application__Group__1__Impl rule__Application__Group__2 ;
    public final void rule__Application__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1359:1: ( rule__Application__Group__1__Impl rule__Application__Group__2 )
            // InternalTyphonDL.g:1360:2: rule__Application__Group__1__Impl rule__Application__Group__2
            {
            pushFollow(FOLLOW_5);
            rule__Application__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Application__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__Group__1"


    // $ANTLR start "rule__Application__Group__1__Impl"
    // InternalTyphonDL.g:1367:1: rule__Application__Group__1__Impl : ( 'application' ) ;
    public final void rule__Application__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1371:1: ( ( 'application' ) )
            // InternalTyphonDL.g:1372:1: ( 'application' )
            {
            // InternalTyphonDL.g:1372:1: ( 'application' )
            // InternalTyphonDL.g:1373:2: 'application'
            {
             before(grammarAccess.getApplicationAccess().getApplicationKeyword_1()); 
            match(input,23,FOLLOW_2); 
             after(grammarAccess.getApplicationAccess().getApplicationKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__Group__1__Impl"


    // $ANTLR start "rule__Application__Group__2"
    // InternalTyphonDL.g:1382:1: rule__Application__Group__2 : rule__Application__Group__2__Impl rule__Application__Group__3 ;
    public final void rule__Application__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1386:1: ( rule__Application__Group__2__Impl rule__Application__Group__3 )
            // InternalTyphonDL.g:1387:2: rule__Application__Group__2__Impl rule__Application__Group__3
            {
            pushFollow(FOLLOW_8);
            rule__Application__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Application__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__Group__2"


    // $ANTLR start "rule__Application__Group__2__Impl"
    // InternalTyphonDL.g:1394:1: rule__Application__Group__2__Impl : ( ( rule__Application__NameAssignment_2 ) ) ;
    public final void rule__Application__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1398:1: ( ( ( rule__Application__NameAssignment_2 ) ) )
            // InternalTyphonDL.g:1399:1: ( ( rule__Application__NameAssignment_2 ) )
            {
            // InternalTyphonDL.g:1399:1: ( ( rule__Application__NameAssignment_2 ) )
            // InternalTyphonDL.g:1400:2: ( rule__Application__NameAssignment_2 )
            {
             before(grammarAccess.getApplicationAccess().getNameAssignment_2()); 
            // InternalTyphonDL.g:1401:2: ( rule__Application__NameAssignment_2 )
            // InternalTyphonDL.g:1401:3: rule__Application__NameAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__Application__NameAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getApplicationAccess().getNameAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__Group__2__Impl"


    // $ANTLR start "rule__Application__Group__3"
    // InternalTyphonDL.g:1409:1: rule__Application__Group__3 : rule__Application__Group__3__Impl rule__Application__Group__4 ;
    public final void rule__Application__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1413:1: ( rule__Application__Group__3__Impl rule__Application__Group__4 )
            // InternalTyphonDL.g:1414:2: rule__Application__Group__3__Impl rule__Application__Group__4
            {
            pushFollow(FOLLOW_15);
            rule__Application__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Application__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__Group__3"


    // $ANTLR start "rule__Application__Group__3__Impl"
    // InternalTyphonDL.g:1421:1: rule__Application__Group__3__Impl : ( '{' ) ;
    public final void rule__Application__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1425:1: ( ( '{' ) )
            // InternalTyphonDL.g:1426:1: ( '{' )
            {
            // InternalTyphonDL.g:1426:1: ( '{' )
            // InternalTyphonDL.g:1427:2: '{'
            {
             before(grammarAccess.getApplicationAccess().getLeftCurlyBracketKeyword_3()); 
            match(input,20,FOLLOW_2); 
             after(grammarAccess.getApplicationAccess().getLeftCurlyBracketKeyword_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__Group__3__Impl"


    // $ANTLR start "rule__Application__Group__4"
    // InternalTyphonDL.g:1436:1: rule__Application__Group__4 : rule__Application__Group__4__Impl rule__Application__Group__5 ;
    public final void rule__Application__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1440:1: ( rule__Application__Group__4__Impl rule__Application__Group__5 )
            // InternalTyphonDL.g:1441:2: rule__Application__Group__4__Impl rule__Application__Group__5
            {
            pushFollow(FOLLOW_15);
            rule__Application__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Application__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__Group__4"


    // $ANTLR start "rule__Application__Group__4__Impl"
    // InternalTyphonDL.g:1448:1: rule__Application__Group__4__Impl : ( ( rule__Application__ContainersAssignment_4 )* ) ;
    public final void rule__Application__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1452:1: ( ( ( rule__Application__ContainersAssignment_4 )* ) )
            // InternalTyphonDL.g:1453:1: ( ( rule__Application__ContainersAssignment_4 )* )
            {
            // InternalTyphonDL.g:1453:1: ( ( rule__Application__ContainersAssignment_4 )* )
            // InternalTyphonDL.g:1454:2: ( rule__Application__ContainersAssignment_4 )*
            {
             before(grammarAccess.getApplicationAccess().getContainersAssignment_4()); 
            // InternalTyphonDL.g:1455:2: ( rule__Application__ContainersAssignment_4 )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==24) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // InternalTyphonDL.g:1455:3: rule__Application__ContainersAssignment_4
            	    {
            	    pushFollow(FOLLOW_16);
            	    rule__Application__ContainersAssignment_4();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);

             after(grammarAccess.getApplicationAccess().getContainersAssignment_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__Group__4__Impl"


    // $ANTLR start "rule__Application__Group__5"
    // InternalTyphonDL.g:1463:1: rule__Application__Group__5 : rule__Application__Group__5__Impl ;
    public final void rule__Application__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1467:1: ( rule__Application__Group__5__Impl )
            // InternalTyphonDL.g:1468:2: rule__Application__Group__5__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Application__Group__5__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__Group__5"


    // $ANTLR start "rule__Application__Group__5__Impl"
    // InternalTyphonDL.g:1474:1: rule__Application__Group__5__Impl : ( '}' ) ;
    public final void rule__Application__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1478:1: ( ( '}' ) )
            // InternalTyphonDL.g:1479:1: ( '}' )
            {
            // InternalTyphonDL.g:1479:1: ( '}' )
            // InternalTyphonDL.g:1480:2: '}'
            {
             before(grammarAccess.getApplicationAccess().getRightCurlyBracketKeyword_5()); 
            match(input,21,FOLLOW_2); 
             after(grammarAccess.getApplicationAccess().getRightCurlyBracketKeyword_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__Group__5__Impl"


    // $ANTLR start "rule__Container__Group__0"
    // InternalTyphonDL.g:1490:1: rule__Container__Group__0 : rule__Container__Group__0__Impl rule__Container__Group__1 ;
    public final void rule__Container__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1494:1: ( rule__Container__Group__0__Impl rule__Container__Group__1 )
            // InternalTyphonDL.g:1495:2: rule__Container__Group__0__Impl rule__Container__Group__1
            {
            pushFollow(FOLLOW_17);
            rule__Container__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Container__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__Group__0"


    // $ANTLR start "rule__Container__Group__0__Impl"
    // InternalTyphonDL.g:1502:1: rule__Container__Group__0__Impl : ( () ) ;
    public final void rule__Container__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1506:1: ( ( () ) )
            // InternalTyphonDL.g:1507:1: ( () )
            {
            // InternalTyphonDL.g:1507:1: ( () )
            // InternalTyphonDL.g:1508:2: ()
            {
             before(grammarAccess.getContainerAccess().getContainerAction_0()); 
            // InternalTyphonDL.g:1509:2: ()
            // InternalTyphonDL.g:1509:3: 
            {
            }

             after(grammarAccess.getContainerAccess().getContainerAction_0()); 

            }


            }

        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__Group__0__Impl"


    // $ANTLR start "rule__Container__Group__1"
    // InternalTyphonDL.g:1517:1: rule__Container__Group__1 : rule__Container__Group__1__Impl rule__Container__Group__2 ;
    public final void rule__Container__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1521:1: ( rule__Container__Group__1__Impl rule__Container__Group__2 )
            // InternalTyphonDL.g:1522:2: rule__Container__Group__1__Impl rule__Container__Group__2
            {
            pushFollow(FOLLOW_5);
            rule__Container__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Container__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__Group__1"


    // $ANTLR start "rule__Container__Group__1__Impl"
    // InternalTyphonDL.g:1529:1: rule__Container__Group__1__Impl : ( 'container' ) ;
    public final void rule__Container__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1533:1: ( ( 'container' ) )
            // InternalTyphonDL.g:1534:1: ( 'container' )
            {
            // InternalTyphonDL.g:1534:1: ( 'container' )
            // InternalTyphonDL.g:1535:2: 'container'
            {
             before(grammarAccess.getContainerAccess().getContainerKeyword_1()); 
            match(input,24,FOLLOW_2); 
             after(grammarAccess.getContainerAccess().getContainerKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__Group__1__Impl"


    // $ANTLR start "rule__Container__Group__2"
    // InternalTyphonDL.g:1544:1: rule__Container__Group__2 : rule__Container__Group__2__Impl rule__Container__Group__3 ;
    public final void rule__Container__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1548:1: ( rule__Container__Group__2__Impl rule__Container__Group__3 )
            // InternalTyphonDL.g:1549:2: rule__Container__Group__2__Impl rule__Container__Group__3
            {
            pushFollow(FOLLOW_8);
            rule__Container__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Container__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__Group__2"


    // $ANTLR start "rule__Container__Group__2__Impl"
    // InternalTyphonDL.g:1556:1: rule__Container__Group__2__Impl : ( ( rule__Container__NameAssignment_2 ) ) ;
    public final void rule__Container__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1560:1: ( ( ( rule__Container__NameAssignment_2 ) ) )
            // InternalTyphonDL.g:1561:1: ( ( rule__Container__NameAssignment_2 ) )
            {
            // InternalTyphonDL.g:1561:1: ( ( rule__Container__NameAssignment_2 ) )
            // InternalTyphonDL.g:1562:2: ( rule__Container__NameAssignment_2 )
            {
             before(grammarAccess.getContainerAccess().getNameAssignment_2()); 
            // InternalTyphonDL.g:1563:2: ( rule__Container__NameAssignment_2 )
            // InternalTyphonDL.g:1563:3: rule__Container__NameAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__Container__NameAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getContainerAccess().getNameAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__Group__2__Impl"


    // $ANTLR start "rule__Container__Group__3"
    // InternalTyphonDL.g:1571:1: rule__Container__Group__3 : rule__Container__Group__3__Impl rule__Container__Group__4 ;
    public final void rule__Container__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1575:1: ( rule__Container__Group__3__Impl rule__Container__Group__4 )
            // InternalTyphonDL.g:1576:2: rule__Container__Group__3__Impl rule__Container__Group__4
            {
            pushFollow(FOLLOW_18);
            rule__Container__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Container__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__Group__3"


    // $ANTLR start "rule__Container__Group__3__Impl"
    // InternalTyphonDL.g:1583:1: rule__Container__Group__3__Impl : ( '{' ) ;
    public final void rule__Container__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1587:1: ( ( '{' ) )
            // InternalTyphonDL.g:1588:1: ( '{' )
            {
            // InternalTyphonDL.g:1588:1: ( '{' )
            // InternalTyphonDL.g:1589:2: '{'
            {
             before(grammarAccess.getContainerAccess().getLeftCurlyBracketKeyword_3()); 
            match(input,20,FOLLOW_2); 
             after(grammarAccess.getContainerAccess().getLeftCurlyBracketKeyword_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__Group__3__Impl"


    // $ANTLR start "rule__Container__Group__4"
    // InternalTyphonDL.g:1598:1: rule__Container__Group__4 : rule__Container__Group__4__Impl rule__Container__Group__5 ;
    public final void rule__Container__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1602:1: ( rule__Container__Group__4__Impl rule__Container__Group__5 )
            // InternalTyphonDL.g:1603:2: rule__Container__Group__4__Impl rule__Container__Group__5
            {
            pushFollow(FOLLOW_18);
            rule__Container__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Container__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__Group__4"


    // $ANTLR start "rule__Container__Group__4__Impl"
    // InternalTyphonDL.g:1610:1: rule__Container__Group__4__Impl : ( ( rule__Container__Alternatives_4 )* ) ;
    public final void rule__Container__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1614:1: ( ( ( rule__Container__Alternatives_4 )* ) )
            // InternalTyphonDL.g:1615:1: ( ( rule__Container__Alternatives_4 )* )
            {
            // InternalTyphonDL.g:1615:1: ( ( rule__Container__Alternatives_4 )* )
            // InternalTyphonDL.g:1616:2: ( rule__Container__Alternatives_4 )*
            {
             before(grammarAccess.getContainerAccess().getAlternatives_4()); 
            // InternalTyphonDL.g:1617:2: ( rule__Container__Alternatives_4 )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==RULE_ID||LA12_0==29) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // InternalTyphonDL.g:1617:3: rule__Container__Alternatives_4
            	    {
            	    pushFollow(FOLLOW_19);
            	    rule__Container__Alternatives_4();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

             after(grammarAccess.getContainerAccess().getAlternatives_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__Group__4__Impl"


    // $ANTLR start "rule__Container__Group__5"
    // InternalTyphonDL.g:1625:1: rule__Container__Group__5 : rule__Container__Group__5__Impl ;
    public final void rule__Container__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1629:1: ( rule__Container__Group__5__Impl )
            // InternalTyphonDL.g:1630:2: rule__Container__Group__5__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Container__Group__5__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__Group__5"


    // $ANTLR start "rule__Container__Group__5__Impl"
    // InternalTyphonDL.g:1636:1: rule__Container__Group__5__Impl : ( '}' ) ;
    public final void rule__Container__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1640:1: ( ( '}' ) )
            // InternalTyphonDL.g:1641:1: ( '}' )
            {
            // InternalTyphonDL.g:1641:1: ( '}' )
            // InternalTyphonDL.g:1642:2: '}'
            {
             before(grammarAccess.getContainerAccess().getRightCurlyBracketKeyword_5()); 
            match(input,21,FOLLOW_2); 
             after(grammarAccess.getContainerAccess().getRightCurlyBracketKeyword_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__Group__5__Impl"


    // $ANTLR start "rule__DBService__Group__0"
    // InternalTyphonDL.g:1652:1: rule__DBService__Group__0 : rule__DBService__Group__0__Impl rule__DBService__Group__1 ;
    public final void rule__DBService__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1656:1: ( rule__DBService__Group__0__Impl rule__DBService__Group__1 )
            // InternalTyphonDL.g:1657:2: rule__DBService__Group__0__Impl rule__DBService__Group__1
            {
            pushFollow(FOLLOW_5);
            rule__DBService__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__DBService__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBService__Group__0"


    // $ANTLR start "rule__DBService__Group__0__Impl"
    // InternalTyphonDL.g:1664:1: rule__DBService__Group__0__Impl : ( 'dbService' ) ;
    public final void rule__DBService__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1668:1: ( ( 'dbService' ) )
            // InternalTyphonDL.g:1669:1: ( 'dbService' )
            {
            // InternalTyphonDL.g:1669:1: ( 'dbService' )
            // InternalTyphonDL.g:1670:2: 'dbService'
            {
             before(grammarAccess.getDBServiceAccess().getDbServiceKeyword_0()); 
            match(input,25,FOLLOW_2); 
             after(grammarAccess.getDBServiceAccess().getDbServiceKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBService__Group__0__Impl"


    // $ANTLR start "rule__DBService__Group__1"
    // InternalTyphonDL.g:1679:1: rule__DBService__Group__1 : rule__DBService__Group__1__Impl rule__DBService__Group__2 ;
    public final void rule__DBService__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1683:1: ( rule__DBService__Group__1__Impl rule__DBService__Group__2 )
            // InternalTyphonDL.g:1684:2: rule__DBService__Group__1__Impl rule__DBService__Group__2
            {
            pushFollow(FOLLOW_8);
            rule__DBService__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__DBService__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBService__Group__1"


    // $ANTLR start "rule__DBService__Group__1__Impl"
    // InternalTyphonDL.g:1691:1: rule__DBService__Group__1__Impl : ( ( rule__DBService__NameAssignment_1 ) ) ;
    public final void rule__DBService__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1695:1: ( ( ( rule__DBService__NameAssignment_1 ) ) )
            // InternalTyphonDL.g:1696:1: ( ( rule__DBService__NameAssignment_1 ) )
            {
            // InternalTyphonDL.g:1696:1: ( ( rule__DBService__NameAssignment_1 ) )
            // InternalTyphonDL.g:1697:2: ( rule__DBService__NameAssignment_1 )
            {
             before(grammarAccess.getDBServiceAccess().getNameAssignment_1()); 
            // InternalTyphonDL.g:1698:2: ( rule__DBService__NameAssignment_1 )
            // InternalTyphonDL.g:1698:3: rule__DBService__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__DBService__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getDBServiceAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBService__Group__1__Impl"


    // $ANTLR start "rule__DBService__Group__2"
    // InternalTyphonDL.g:1706:1: rule__DBService__Group__2 : rule__DBService__Group__2__Impl rule__DBService__Group__3 ;
    public final void rule__DBService__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1710:1: ( rule__DBService__Group__2__Impl rule__DBService__Group__3 )
            // InternalTyphonDL.g:1711:2: rule__DBService__Group__2__Impl rule__DBService__Group__3
            {
            pushFollow(FOLLOW_18);
            rule__DBService__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__DBService__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBService__Group__2"


    // $ANTLR start "rule__DBService__Group__2__Impl"
    // InternalTyphonDL.g:1718:1: rule__DBService__Group__2__Impl : ( '{' ) ;
    public final void rule__DBService__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1722:1: ( ( '{' ) )
            // InternalTyphonDL.g:1723:1: ( '{' )
            {
            // InternalTyphonDL.g:1723:1: ( '{' )
            // InternalTyphonDL.g:1724:2: '{'
            {
             before(grammarAccess.getDBServiceAccess().getLeftCurlyBracketKeyword_2()); 
            match(input,20,FOLLOW_2); 
             after(grammarAccess.getDBServiceAccess().getLeftCurlyBracketKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBService__Group__2__Impl"


    // $ANTLR start "rule__DBService__Group__3"
    // InternalTyphonDL.g:1733:1: rule__DBService__Group__3 : rule__DBService__Group__3__Impl rule__DBService__Group__4 ;
    public final void rule__DBService__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1737:1: ( rule__DBService__Group__3__Impl rule__DBService__Group__4 )
            // InternalTyphonDL.g:1738:2: rule__DBService__Group__3__Impl rule__DBService__Group__4
            {
            pushFollow(FOLLOW_18);
            rule__DBService__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__DBService__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBService__Group__3"


    // $ANTLR start "rule__DBService__Group__3__Impl"
    // InternalTyphonDL.g:1745:1: rule__DBService__Group__3__Impl : ( ( rule__DBService__FeaturesAssignment_3 )* ) ;
    public final void rule__DBService__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1749:1: ( ( ( rule__DBService__FeaturesAssignment_3 )* ) )
            // InternalTyphonDL.g:1750:1: ( ( rule__DBService__FeaturesAssignment_3 )* )
            {
            // InternalTyphonDL.g:1750:1: ( ( rule__DBService__FeaturesAssignment_3 )* )
            // InternalTyphonDL.g:1751:2: ( rule__DBService__FeaturesAssignment_3 )*
            {
             before(grammarAccess.getDBServiceAccess().getFeaturesAssignment_3()); 
            // InternalTyphonDL.g:1752:2: ( rule__DBService__FeaturesAssignment_3 )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==RULE_ID) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // InternalTyphonDL.g:1752:3: rule__DBService__FeaturesAssignment_3
            	    {
            	    pushFollow(FOLLOW_19);
            	    rule__DBService__FeaturesAssignment_3();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

             after(grammarAccess.getDBServiceAccess().getFeaturesAssignment_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBService__Group__3__Impl"


    // $ANTLR start "rule__DBService__Group__4"
    // InternalTyphonDL.g:1760:1: rule__DBService__Group__4 : rule__DBService__Group__4__Impl ;
    public final void rule__DBService__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1764:1: ( rule__DBService__Group__4__Impl )
            // InternalTyphonDL.g:1765:2: rule__DBService__Group__4__Impl
            {
            pushFollow(FOLLOW_2);
            rule__DBService__Group__4__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBService__Group__4"


    // $ANTLR start "rule__DBService__Group__4__Impl"
    // InternalTyphonDL.g:1771:1: rule__DBService__Group__4__Impl : ( '}' ) ;
    public final void rule__DBService__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1775:1: ( ( '}' ) )
            // InternalTyphonDL.g:1776:1: ( '}' )
            {
            // InternalTyphonDL.g:1776:1: ( '}' )
            // InternalTyphonDL.g:1777:2: '}'
            {
             before(grammarAccess.getDBServiceAccess().getRightCurlyBracketKeyword_4()); 
            match(input,21,FOLLOW_2); 
             after(grammarAccess.getDBServiceAccess().getRightCurlyBracketKeyword_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBService__Group__4__Impl"


    // $ANTLR start "rule__BusinessService__Group__0"
    // InternalTyphonDL.g:1787:1: rule__BusinessService__Group__0 : rule__BusinessService__Group__0__Impl rule__BusinessService__Group__1 ;
    public final void rule__BusinessService__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1791:1: ( rule__BusinessService__Group__0__Impl rule__BusinessService__Group__1 )
            // InternalTyphonDL.g:1792:2: rule__BusinessService__Group__0__Impl rule__BusinessService__Group__1
            {
            pushFollow(FOLLOW_5);
            rule__BusinessService__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__BusinessService__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__BusinessService__Group__0"


    // $ANTLR start "rule__BusinessService__Group__0__Impl"
    // InternalTyphonDL.g:1799:1: rule__BusinessService__Group__0__Impl : ( 'businessService' ) ;
    public final void rule__BusinessService__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1803:1: ( ( 'businessService' ) )
            // InternalTyphonDL.g:1804:1: ( 'businessService' )
            {
            // InternalTyphonDL.g:1804:1: ( 'businessService' )
            // InternalTyphonDL.g:1805:2: 'businessService'
            {
             before(grammarAccess.getBusinessServiceAccess().getBusinessServiceKeyword_0()); 
            match(input,26,FOLLOW_2); 
             after(grammarAccess.getBusinessServiceAccess().getBusinessServiceKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__BusinessService__Group__0__Impl"


    // $ANTLR start "rule__BusinessService__Group__1"
    // InternalTyphonDL.g:1814:1: rule__BusinessService__Group__1 : rule__BusinessService__Group__1__Impl rule__BusinessService__Group__2 ;
    public final void rule__BusinessService__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1818:1: ( rule__BusinessService__Group__1__Impl rule__BusinessService__Group__2 )
            // InternalTyphonDL.g:1819:2: rule__BusinessService__Group__1__Impl rule__BusinessService__Group__2
            {
            pushFollow(FOLLOW_8);
            rule__BusinessService__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__BusinessService__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__BusinessService__Group__1"


    // $ANTLR start "rule__BusinessService__Group__1__Impl"
    // InternalTyphonDL.g:1826:1: rule__BusinessService__Group__1__Impl : ( ( rule__BusinessService__NameAssignment_1 ) ) ;
    public final void rule__BusinessService__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1830:1: ( ( ( rule__BusinessService__NameAssignment_1 ) ) )
            // InternalTyphonDL.g:1831:1: ( ( rule__BusinessService__NameAssignment_1 ) )
            {
            // InternalTyphonDL.g:1831:1: ( ( rule__BusinessService__NameAssignment_1 ) )
            // InternalTyphonDL.g:1832:2: ( rule__BusinessService__NameAssignment_1 )
            {
             before(grammarAccess.getBusinessServiceAccess().getNameAssignment_1()); 
            // InternalTyphonDL.g:1833:2: ( rule__BusinessService__NameAssignment_1 )
            // InternalTyphonDL.g:1833:3: rule__BusinessService__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__BusinessService__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getBusinessServiceAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__BusinessService__Group__1__Impl"


    // $ANTLR start "rule__BusinessService__Group__2"
    // InternalTyphonDL.g:1841:1: rule__BusinessService__Group__2 : rule__BusinessService__Group__2__Impl rule__BusinessService__Group__3 ;
    public final void rule__BusinessService__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1845:1: ( rule__BusinessService__Group__2__Impl rule__BusinessService__Group__3 )
            // InternalTyphonDL.g:1846:2: rule__BusinessService__Group__2__Impl rule__BusinessService__Group__3
            {
            pushFollow(FOLLOW_18);
            rule__BusinessService__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__BusinessService__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__BusinessService__Group__2"


    // $ANTLR start "rule__BusinessService__Group__2__Impl"
    // InternalTyphonDL.g:1853:1: rule__BusinessService__Group__2__Impl : ( '{' ) ;
    public final void rule__BusinessService__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1857:1: ( ( '{' ) )
            // InternalTyphonDL.g:1858:1: ( '{' )
            {
            // InternalTyphonDL.g:1858:1: ( '{' )
            // InternalTyphonDL.g:1859:2: '{'
            {
             before(grammarAccess.getBusinessServiceAccess().getLeftCurlyBracketKeyword_2()); 
            match(input,20,FOLLOW_2); 
             after(grammarAccess.getBusinessServiceAccess().getLeftCurlyBracketKeyword_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__BusinessService__Group__2__Impl"


    // $ANTLR start "rule__BusinessService__Group__3"
    // InternalTyphonDL.g:1868:1: rule__BusinessService__Group__3 : rule__BusinessService__Group__3__Impl rule__BusinessService__Group__4 ;
    public final void rule__BusinessService__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1872:1: ( rule__BusinessService__Group__3__Impl rule__BusinessService__Group__4 )
            // InternalTyphonDL.g:1873:2: rule__BusinessService__Group__3__Impl rule__BusinessService__Group__4
            {
            pushFollow(FOLLOW_18);
            rule__BusinessService__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__BusinessService__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__BusinessService__Group__3"


    // $ANTLR start "rule__BusinessService__Group__3__Impl"
    // InternalTyphonDL.g:1880:1: rule__BusinessService__Group__3__Impl : ( ( rule__BusinessService__FeaturesAssignment_3 )* ) ;
    public final void rule__BusinessService__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1884:1: ( ( ( rule__BusinessService__FeaturesAssignment_3 )* ) )
            // InternalTyphonDL.g:1885:1: ( ( rule__BusinessService__FeaturesAssignment_3 )* )
            {
            // InternalTyphonDL.g:1885:1: ( ( rule__BusinessService__FeaturesAssignment_3 )* )
            // InternalTyphonDL.g:1886:2: ( rule__BusinessService__FeaturesAssignment_3 )*
            {
             before(grammarAccess.getBusinessServiceAccess().getFeaturesAssignment_3()); 
            // InternalTyphonDL.g:1887:2: ( rule__BusinessService__FeaturesAssignment_3 )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==RULE_ID) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // InternalTyphonDL.g:1887:3: rule__BusinessService__FeaturesAssignment_3
            	    {
            	    pushFollow(FOLLOW_19);
            	    rule__BusinessService__FeaturesAssignment_3();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

             after(grammarAccess.getBusinessServiceAccess().getFeaturesAssignment_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__BusinessService__Group__3__Impl"


    // $ANTLR start "rule__BusinessService__Group__4"
    // InternalTyphonDL.g:1895:1: rule__BusinessService__Group__4 : rule__BusinessService__Group__4__Impl ;
    public final void rule__BusinessService__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1899:1: ( rule__BusinessService__Group__4__Impl )
            // InternalTyphonDL.g:1900:2: rule__BusinessService__Group__4__Impl
            {
            pushFollow(FOLLOW_2);
            rule__BusinessService__Group__4__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__BusinessService__Group__4"


    // $ANTLR start "rule__BusinessService__Group__4__Impl"
    // InternalTyphonDL.g:1906:1: rule__BusinessService__Group__4__Impl : ( '}' ) ;
    public final void rule__BusinessService__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1910:1: ( ( '}' ) )
            // InternalTyphonDL.g:1911:1: ( '}' )
            {
            // InternalTyphonDL.g:1911:1: ( '}' )
            // InternalTyphonDL.g:1912:2: '}'
            {
             before(grammarAccess.getBusinessServiceAccess().getRightCurlyBracketKeyword_4()); 
            match(input,21,FOLLOW_2); 
             after(grammarAccess.getBusinessServiceAccess().getRightCurlyBracketKeyword_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__BusinessService__Group__4__Impl"


    // $ANTLR start "rule__Entity__Group__0"
    // InternalTyphonDL.g:1922:1: rule__Entity__Group__0 : rule__Entity__Group__0__Impl rule__Entity__Group__1 ;
    public final void rule__Entity__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1926:1: ( rule__Entity__Group__0__Impl rule__Entity__Group__1 )
            // InternalTyphonDL.g:1927:2: rule__Entity__Group__0__Impl rule__Entity__Group__1
            {
            pushFollow(FOLLOW_5);
            rule__Entity__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Entity__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group__0"


    // $ANTLR start "rule__Entity__Group__0__Impl"
    // InternalTyphonDL.g:1934:1: rule__Entity__Group__0__Impl : ( 'entity' ) ;
    public final void rule__Entity__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1938:1: ( ( 'entity' ) )
            // InternalTyphonDL.g:1939:1: ( 'entity' )
            {
            // InternalTyphonDL.g:1939:1: ( 'entity' )
            // InternalTyphonDL.g:1940:2: 'entity'
            {
             before(grammarAccess.getEntityAccess().getEntityKeyword_0()); 
            match(input,27,FOLLOW_2); 
             after(grammarAccess.getEntityAccess().getEntityKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group__0__Impl"


    // $ANTLR start "rule__Entity__Group__1"
    // InternalTyphonDL.g:1949:1: rule__Entity__Group__1 : rule__Entity__Group__1__Impl rule__Entity__Group__2 ;
    public final void rule__Entity__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1953:1: ( rule__Entity__Group__1__Impl rule__Entity__Group__2 )
            // InternalTyphonDL.g:1954:2: rule__Entity__Group__1__Impl rule__Entity__Group__2
            {
            pushFollow(FOLLOW_20);
            rule__Entity__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Entity__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group__1"


    // $ANTLR start "rule__Entity__Group__1__Impl"
    // InternalTyphonDL.g:1961:1: rule__Entity__Group__1__Impl : ( ( rule__Entity__NameAssignment_1 ) ) ;
    public final void rule__Entity__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1965:1: ( ( ( rule__Entity__NameAssignment_1 ) ) )
            // InternalTyphonDL.g:1966:1: ( ( rule__Entity__NameAssignment_1 ) )
            {
            // InternalTyphonDL.g:1966:1: ( ( rule__Entity__NameAssignment_1 ) )
            // InternalTyphonDL.g:1967:2: ( rule__Entity__NameAssignment_1 )
            {
             before(grammarAccess.getEntityAccess().getNameAssignment_1()); 
            // InternalTyphonDL.g:1968:2: ( rule__Entity__NameAssignment_1 )
            // InternalTyphonDL.g:1968:3: rule__Entity__NameAssignment_1
            {
            pushFollow(FOLLOW_2);
            rule__Entity__NameAssignment_1();

            state._fsp--;


            }

             after(grammarAccess.getEntityAccess().getNameAssignment_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group__1__Impl"


    // $ANTLR start "rule__Entity__Group__2"
    // InternalTyphonDL.g:1976:1: rule__Entity__Group__2 : rule__Entity__Group__2__Impl rule__Entity__Group__3 ;
    public final void rule__Entity__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1980:1: ( rule__Entity__Group__2__Impl rule__Entity__Group__3 )
            // InternalTyphonDL.g:1981:2: rule__Entity__Group__2__Impl rule__Entity__Group__3
            {
            pushFollow(FOLLOW_20);
            rule__Entity__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Entity__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group__2"


    // $ANTLR start "rule__Entity__Group__2__Impl"
    // InternalTyphonDL.g:1988:1: rule__Entity__Group__2__Impl : ( ( rule__Entity__Group_2__0 )? ) ;
    public final void rule__Entity__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:1992:1: ( ( ( rule__Entity__Group_2__0 )? ) )
            // InternalTyphonDL.g:1993:1: ( ( rule__Entity__Group_2__0 )? )
            {
            // InternalTyphonDL.g:1993:1: ( ( rule__Entity__Group_2__0 )? )
            // InternalTyphonDL.g:1994:2: ( rule__Entity__Group_2__0 )?
            {
             before(grammarAccess.getEntityAccess().getGroup_2()); 
            // InternalTyphonDL.g:1995:2: ( rule__Entity__Group_2__0 )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==28) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // InternalTyphonDL.g:1995:3: rule__Entity__Group_2__0
                    {
                    pushFollow(FOLLOW_2);
                    rule__Entity__Group_2__0();

                    state._fsp--;


                    }
                    break;

            }

             after(grammarAccess.getEntityAccess().getGroup_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group__2__Impl"


    // $ANTLR start "rule__Entity__Group__3"
    // InternalTyphonDL.g:2003:1: rule__Entity__Group__3 : rule__Entity__Group__3__Impl rule__Entity__Group__4 ;
    public final void rule__Entity__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2007:1: ( rule__Entity__Group__3__Impl rule__Entity__Group__4 )
            // InternalTyphonDL.g:2008:2: rule__Entity__Group__3__Impl rule__Entity__Group__4
            {
            pushFollow(FOLLOW_18);
            rule__Entity__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Entity__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group__3"


    // $ANTLR start "rule__Entity__Group__3__Impl"
    // InternalTyphonDL.g:2015:1: rule__Entity__Group__3__Impl : ( '{' ) ;
    public final void rule__Entity__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2019:1: ( ( '{' ) )
            // InternalTyphonDL.g:2020:1: ( '{' )
            {
            // InternalTyphonDL.g:2020:1: ( '{' )
            // InternalTyphonDL.g:2021:2: '{'
            {
             before(grammarAccess.getEntityAccess().getLeftCurlyBracketKeyword_3()); 
            match(input,20,FOLLOW_2); 
             after(grammarAccess.getEntityAccess().getLeftCurlyBracketKeyword_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group__3__Impl"


    // $ANTLR start "rule__Entity__Group__4"
    // InternalTyphonDL.g:2030:1: rule__Entity__Group__4 : rule__Entity__Group__4__Impl rule__Entity__Group__5 ;
    public final void rule__Entity__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2034:1: ( rule__Entity__Group__4__Impl rule__Entity__Group__5 )
            // InternalTyphonDL.g:2035:2: rule__Entity__Group__4__Impl rule__Entity__Group__5
            {
            pushFollow(FOLLOW_18);
            rule__Entity__Group__4__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Entity__Group__5();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group__4"


    // $ANTLR start "rule__Entity__Group__4__Impl"
    // InternalTyphonDL.g:2042:1: rule__Entity__Group__4__Impl : ( ( rule__Entity__FeaturesAssignment_4 )* ) ;
    public final void rule__Entity__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2046:1: ( ( ( rule__Entity__FeaturesAssignment_4 )* ) )
            // InternalTyphonDL.g:2047:1: ( ( rule__Entity__FeaturesAssignment_4 )* )
            {
            // InternalTyphonDL.g:2047:1: ( ( rule__Entity__FeaturesAssignment_4 )* )
            // InternalTyphonDL.g:2048:2: ( rule__Entity__FeaturesAssignment_4 )*
            {
             before(grammarAccess.getEntityAccess().getFeaturesAssignment_4()); 
            // InternalTyphonDL.g:2049:2: ( rule__Entity__FeaturesAssignment_4 )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==RULE_ID) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // InternalTyphonDL.g:2049:3: rule__Entity__FeaturesAssignment_4
            	    {
            	    pushFollow(FOLLOW_19);
            	    rule__Entity__FeaturesAssignment_4();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

             after(grammarAccess.getEntityAccess().getFeaturesAssignment_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group__4__Impl"


    // $ANTLR start "rule__Entity__Group__5"
    // InternalTyphonDL.g:2057:1: rule__Entity__Group__5 : rule__Entity__Group__5__Impl ;
    public final void rule__Entity__Group__5() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2061:1: ( rule__Entity__Group__5__Impl )
            // InternalTyphonDL.g:2062:2: rule__Entity__Group__5__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Entity__Group__5__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group__5"


    // $ANTLR start "rule__Entity__Group__5__Impl"
    // InternalTyphonDL.g:2068:1: rule__Entity__Group__5__Impl : ( '}' ) ;
    public final void rule__Entity__Group__5__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2072:1: ( ( '}' ) )
            // InternalTyphonDL.g:2073:1: ( '}' )
            {
            // InternalTyphonDL.g:2073:1: ( '}' )
            // InternalTyphonDL.g:2074:2: '}'
            {
             before(grammarAccess.getEntityAccess().getRightCurlyBracketKeyword_5()); 
            match(input,21,FOLLOW_2); 
             after(grammarAccess.getEntityAccess().getRightCurlyBracketKeyword_5()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group__5__Impl"


    // $ANTLR start "rule__Entity__Group_2__0"
    // InternalTyphonDL.g:2084:1: rule__Entity__Group_2__0 : rule__Entity__Group_2__0__Impl rule__Entity__Group_2__1 ;
    public final void rule__Entity__Group_2__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2088:1: ( rule__Entity__Group_2__0__Impl rule__Entity__Group_2__1 )
            // InternalTyphonDL.g:2089:2: rule__Entity__Group_2__0__Impl rule__Entity__Group_2__1
            {
            pushFollow(FOLLOW_5);
            rule__Entity__Group_2__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Entity__Group_2__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group_2__0"


    // $ANTLR start "rule__Entity__Group_2__0__Impl"
    // InternalTyphonDL.g:2096:1: rule__Entity__Group_2__0__Impl : ( 'extends' ) ;
    public final void rule__Entity__Group_2__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2100:1: ( ( 'extends' ) )
            // InternalTyphonDL.g:2101:1: ( 'extends' )
            {
            // InternalTyphonDL.g:2101:1: ( 'extends' )
            // InternalTyphonDL.g:2102:2: 'extends'
            {
             before(grammarAccess.getEntityAccess().getExtendsKeyword_2_0()); 
            match(input,28,FOLLOW_2); 
             after(grammarAccess.getEntityAccess().getExtendsKeyword_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group_2__0__Impl"


    // $ANTLR start "rule__Entity__Group_2__1"
    // InternalTyphonDL.g:2111:1: rule__Entity__Group_2__1 : rule__Entity__Group_2__1__Impl ;
    public final void rule__Entity__Group_2__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2115:1: ( rule__Entity__Group_2__1__Impl )
            // InternalTyphonDL.g:2116:2: rule__Entity__Group_2__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Entity__Group_2__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group_2__1"


    // $ANTLR start "rule__Entity__Group_2__1__Impl"
    // InternalTyphonDL.g:2122:1: rule__Entity__Group_2__1__Impl : ( ( rule__Entity__SuperTypeAssignment_2_1 ) ) ;
    public final void rule__Entity__Group_2__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2126:1: ( ( ( rule__Entity__SuperTypeAssignment_2_1 ) ) )
            // InternalTyphonDL.g:2127:1: ( ( rule__Entity__SuperTypeAssignment_2_1 ) )
            {
            // InternalTyphonDL.g:2127:1: ( ( rule__Entity__SuperTypeAssignment_2_1 ) )
            // InternalTyphonDL.g:2128:2: ( rule__Entity__SuperTypeAssignment_2_1 )
            {
             before(grammarAccess.getEntityAccess().getSuperTypeAssignment_2_1()); 
            // InternalTyphonDL.g:2129:2: ( rule__Entity__SuperTypeAssignment_2_1 )
            // InternalTyphonDL.g:2129:3: rule__Entity__SuperTypeAssignment_2_1
            {
            pushFollow(FOLLOW_2);
            rule__Entity__SuperTypeAssignment_2_1();

            state._fsp--;


            }

             after(grammarAccess.getEntityAccess().getSuperTypeAssignment_2_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__Group_2__1__Impl"


    // $ANTLR start "rule__EnvList__Group__0"
    // InternalTyphonDL.g:2138:1: rule__EnvList__Group__0 : rule__EnvList__Group__0__Impl rule__EnvList__Group__1 ;
    public final void rule__EnvList__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2142:1: ( rule__EnvList__Group__0__Impl rule__EnvList__Group__1 )
            // InternalTyphonDL.g:2143:2: rule__EnvList__Group__0__Impl rule__EnvList__Group__1
            {
            pushFollow(FOLLOW_8);
            rule__EnvList__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__EnvList__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__EnvList__Group__0"


    // $ANTLR start "rule__EnvList__Group__0__Impl"
    // InternalTyphonDL.g:2150:1: rule__EnvList__Group__0__Impl : ( 'environment' ) ;
    public final void rule__EnvList__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2154:1: ( ( 'environment' ) )
            // InternalTyphonDL.g:2155:1: ( 'environment' )
            {
            // InternalTyphonDL.g:2155:1: ( 'environment' )
            // InternalTyphonDL.g:2156:2: 'environment'
            {
             before(grammarAccess.getEnvListAccess().getEnvironmentKeyword_0()); 
            match(input,29,FOLLOW_2); 
             after(grammarAccess.getEnvListAccess().getEnvironmentKeyword_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__EnvList__Group__0__Impl"


    // $ANTLR start "rule__EnvList__Group__1"
    // InternalTyphonDL.g:2165:1: rule__EnvList__Group__1 : rule__EnvList__Group__1__Impl rule__EnvList__Group__2 ;
    public final void rule__EnvList__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2169:1: ( rule__EnvList__Group__1__Impl rule__EnvList__Group__2 )
            // InternalTyphonDL.g:2170:2: rule__EnvList__Group__1__Impl rule__EnvList__Group__2
            {
            pushFollow(FOLLOW_21);
            rule__EnvList__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__EnvList__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__EnvList__Group__1"


    // $ANTLR start "rule__EnvList__Group__1__Impl"
    // InternalTyphonDL.g:2177:1: rule__EnvList__Group__1__Impl : ( '{' ) ;
    public final void rule__EnvList__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2181:1: ( ( '{' ) )
            // InternalTyphonDL.g:2182:1: ( '{' )
            {
            // InternalTyphonDL.g:2182:1: ( '{' )
            // InternalTyphonDL.g:2183:2: '{'
            {
             before(grammarAccess.getEnvListAccess().getLeftCurlyBracketKeyword_1()); 
            match(input,20,FOLLOW_2); 
             after(grammarAccess.getEnvListAccess().getLeftCurlyBracketKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__EnvList__Group__1__Impl"


    // $ANTLR start "rule__EnvList__Group__2"
    // InternalTyphonDL.g:2192:1: rule__EnvList__Group__2 : rule__EnvList__Group__2__Impl rule__EnvList__Group__3 ;
    public final void rule__EnvList__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2196:1: ( rule__EnvList__Group__2__Impl rule__EnvList__Group__3 )
            // InternalTyphonDL.g:2197:2: rule__EnvList__Group__2__Impl rule__EnvList__Group__3
            {
            pushFollow(FOLLOW_22);
            rule__EnvList__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__EnvList__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__EnvList__Group__2"


    // $ANTLR start "rule__EnvList__Group__2__Impl"
    // InternalTyphonDL.g:2204:1: rule__EnvList__Group__2__Impl : ( ( ( rule__EnvList__EnvironmentVarsAssignment_2 ) ) ( ( rule__EnvList__EnvironmentVarsAssignment_2 )* ) ) ;
    public final void rule__EnvList__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2208:1: ( ( ( ( rule__EnvList__EnvironmentVarsAssignment_2 ) ) ( ( rule__EnvList__EnvironmentVarsAssignment_2 )* ) ) )
            // InternalTyphonDL.g:2209:1: ( ( ( rule__EnvList__EnvironmentVarsAssignment_2 ) ) ( ( rule__EnvList__EnvironmentVarsAssignment_2 )* ) )
            {
            // InternalTyphonDL.g:2209:1: ( ( ( rule__EnvList__EnvironmentVarsAssignment_2 ) ) ( ( rule__EnvList__EnvironmentVarsAssignment_2 )* ) )
            // InternalTyphonDL.g:2210:2: ( ( rule__EnvList__EnvironmentVarsAssignment_2 ) ) ( ( rule__EnvList__EnvironmentVarsAssignment_2 )* )
            {
            // InternalTyphonDL.g:2210:2: ( ( rule__EnvList__EnvironmentVarsAssignment_2 ) )
            // InternalTyphonDL.g:2211:3: ( rule__EnvList__EnvironmentVarsAssignment_2 )
            {
             before(grammarAccess.getEnvListAccess().getEnvironmentVarsAssignment_2()); 
            // InternalTyphonDL.g:2212:3: ( rule__EnvList__EnvironmentVarsAssignment_2 )
            // InternalTyphonDL.g:2212:4: rule__EnvList__EnvironmentVarsAssignment_2
            {
            pushFollow(FOLLOW_23);
            rule__EnvList__EnvironmentVarsAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getEnvListAccess().getEnvironmentVarsAssignment_2()); 

            }

            // InternalTyphonDL.g:2215:2: ( ( rule__EnvList__EnvironmentVarsAssignment_2 )* )
            // InternalTyphonDL.g:2216:3: ( rule__EnvList__EnvironmentVarsAssignment_2 )*
            {
             before(grammarAccess.getEnvListAccess().getEnvironmentVarsAssignment_2()); 
            // InternalTyphonDL.g:2217:3: ( rule__EnvList__EnvironmentVarsAssignment_2 )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==RULE_MYSTRING) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // InternalTyphonDL.g:2217:4: rule__EnvList__EnvironmentVarsAssignment_2
            	    {
            	    pushFollow(FOLLOW_23);
            	    rule__EnvList__EnvironmentVarsAssignment_2();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

             after(grammarAccess.getEnvListAccess().getEnvironmentVarsAssignment_2()); 

            }


            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__EnvList__Group__2__Impl"


    // $ANTLR start "rule__EnvList__Group__3"
    // InternalTyphonDL.g:2226:1: rule__EnvList__Group__3 : rule__EnvList__Group__3__Impl ;
    public final void rule__EnvList__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2230:1: ( rule__EnvList__Group__3__Impl )
            // InternalTyphonDL.g:2231:2: rule__EnvList__Group__3__Impl
            {
            pushFollow(FOLLOW_2);
            rule__EnvList__Group__3__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__EnvList__Group__3"


    // $ANTLR start "rule__EnvList__Group__3__Impl"
    // InternalTyphonDL.g:2237:1: rule__EnvList__Group__3__Impl : ( '}' ) ;
    public final void rule__EnvList__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2241:1: ( ( '}' ) )
            // InternalTyphonDL.g:2242:1: ( '}' )
            {
            // InternalTyphonDL.g:2242:1: ( '}' )
            // InternalTyphonDL.g:2243:2: '}'
            {
             before(grammarAccess.getEnvListAccess().getRightCurlyBracketKeyword_3()); 
            match(input,21,FOLLOW_2); 
             after(grammarAccess.getEnvListAccess().getRightCurlyBracketKeyword_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__EnvList__Group__3__Impl"


    // $ANTLR start "rule__AssignmentList__Group__0"
    // InternalTyphonDL.g:2253:1: rule__AssignmentList__Group__0 : rule__AssignmentList__Group__0__Impl rule__AssignmentList__Group__1 ;
    public final void rule__AssignmentList__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2257:1: ( rule__AssignmentList__Group__0__Impl rule__AssignmentList__Group__1 )
            // InternalTyphonDL.g:2258:2: rule__AssignmentList__Group__0__Impl rule__AssignmentList__Group__1
            {
            pushFollow(FOLLOW_8);
            rule__AssignmentList__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__AssignmentList__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AssignmentList__Group__0"


    // $ANTLR start "rule__AssignmentList__Group__0__Impl"
    // InternalTyphonDL.g:2265:1: rule__AssignmentList__Group__0__Impl : ( ( rule__AssignmentList__NameAssignment_0 ) ) ;
    public final void rule__AssignmentList__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2269:1: ( ( ( rule__AssignmentList__NameAssignment_0 ) ) )
            // InternalTyphonDL.g:2270:1: ( ( rule__AssignmentList__NameAssignment_0 ) )
            {
            // InternalTyphonDL.g:2270:1: ( ( rule__AssignmentList__NameAssignment_0 ) )
            // InternalTyphonDL.g:2271:2: ( rule__AssignmentList__NameAssignment_0 )
            {
             before(grammarAccess.getAssignmentListAccess().getNameAssignment_0()); 
            // InternalTyphonDL.g:2272:2: ( rule__AssignmentList__NameAssignment_0 )
            // InternalTyphonDL.g:2272:3: rule__AssignmentList__NameAssignment_0
            {
            pushFollow(FOLLOW_2);
            rule__AssignmentList__NameAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getAssignmentListAccess().getNameAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AssignmentList__Group__0__Impl"


    // $ANTLR start "rule__AssignmentList__Group__1"
    // InternalTyphonDL.g:2280:1: rule__AssignmentList__Group__1 : rule__AssignmentList__Group__1__Impl rule__AssignmentList__Group__2 ;
    public final void rule__AssignmentList__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2284:1: ( rule__AssignmentList__Group__1__Impl rule__AssignmentList__Group__2 )
            // InternalTyphonDL.g:2285:2: rule__AssignmentList__Group__1__Impl rule__AssignmentList__Group__2
            {
            pushFollow(FOLLOW_5);
            rule__AssignmentList__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__AssignmentList__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AssignmentList__Group__1"


    // $ANTLR start "rule__AssignmentList__Group__1__Impl"
    // InternalTyphonDL.g:2292:1: rule__AssignmentList__Group__1__Impl : ( '{' ) ;
    public final void rule__AssignmentList__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2296:1: ( ( '{' ) )
            // InternalTyphonDL.g:2297:1: ( '{' )
            {
            // InternalTyphonDL.g:2297:1: ( '{' )
            // InternalTyphonDL.g:2298:2: '{'
            {
             before(grammarAccess.getAssignmentListAccess().getLeftCurlyBracketKeyword_1()); 
            match(input,20,FOLLOW_2); 
             after(grammarAccess.getAssignmentListAccess().getLeftCurlyBracketKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AssignmentList__Group__1__Impl"


    // $ANTLR start "rule__AssignmentList__Group__2"
    // InternalTyphonDL.g:2307:1: rule__AssignmentList__Group__2 : rule__AssignmentList__Group__2__Impl rule__AssignmentList__Group__3 ;
    public final void rule__AssignmentList__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2311:1: ( rule__AssignmentList__Group__2__Impl rule__AssignmentList__Group__3 )
            // InternalTyphonDL.g:2312:2: rule__AssignmentList__Group__2__Impl rule__AssignmentList__Group__3
            {
            pushFollow(FOLLOW_22);
            rule__AssignmentList__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__AssignmentList__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AssignmentList__Group__2"


    // $ANTLR start "rule__AssignmentList__Group__2__Impl"
    // InternalTyphonDL.g:2319:1: rule__AssignmentList__Group__2__Impl : ( ( ( rule__AssignmentList__AssignmentsAssignment_2 ) ) ( ( rule__AssignmentList__AssignmentsAssignment_2 )* ) ) ;
    public final void rule__AssignmentList__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2323:1: ( ( ( ( rule__AssignmentList__AssignmentsAssignment_2 ) ) ( ( rule__AssignmentList__AssignmentsAssignment_2 )* ) ) )
            // InternalTyphonDL.g:2324:1: ( ( ( rule__AssignmentList__AssignmentsAssignment_2 ) ) ( ( rule__AssignmentList__AssignmentsAssignment_2 )* ) )
            {
            // InternalTyphonDL.g:2324:1: ( ( ( rule__AssignmentList__AssignmentsAssignment_2 ) ) ( ( rule__AssignmentList__AssignmentsAssignment_2 )* ) )
            // InternalTyphonDL.g:2325:2: ( ( rule__AssignmentList__AssignmentsAssignment_2 ) ) ( ( rule__AssignmentList__AssignmentsAssignment_2 )* )
            {
            // InternalTyphonDL.g:2325:2: ( ( rule__AssignmentList__AssignmentsAssignment_2 ) )
            // InternalTyphonDL.g:2326:3: ( rule__AssignmentList__AssignmentsAssignment_2 )
            {
             before(grammarAccess.getAssignmentListAccess().getAssignmentsAssignment_2()); 
            // InternalTyphonDL.g:2327:3: ( rule__AssignmentList__AssignmentsAssignment_2 )
            // InternalTyphonDL.g:2327:4: rule__AssignmentList__AssignmentsAssignment_2
            {
            pushFollow(FOLLOW_24);
            rule__AssignmentList__AssignmentsAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getAssignmentListAccess().getAssignmentsAssignment_2()); 

            }

            // InternalTyphonDL.g:2330:2: ( ( rule__AssignmentList__AssignmentsAssignment_2 )* )
            // InternalTyphonDL.g:2331:3: ( rule__AssignmentList__AssignmentsAssignment_2 )*
            {
             before(grammarAccess.getAssignmentListAccess().getAssignmentsAssignment_2()); 
            // InternalTyphonDL.g:2332:3: ( rule__AssignmentList__AssignmentsAssignment_2 )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==RULE_ID) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // InternalTyphonDL.g:2332:4: rule__AssignmentList__AssignmentsAssignment_2
            	    {
            	    pushFollow(FOLLOW_24);
            	    rule__AssignmentList__AssignmentsAssignment_2();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);

             after(grammarAccess.getAssignmentListAccess().getAssignmentsAssignment_2()); 

            }


            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AssignmentList__Group__2__Impl"


    // $ANTLR start "rule__AssignmentList__Group__3"
    // InternalTyphonDL.g:2341:1: rule__AssignmentList__Group__3 : rule__AssignmentList__Group__3__Impl ;
    public final void rule__AssignmentList__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2345:1: ( rule__AssignmentList__Group__3__Impl )
            // InternalTyphonDL.g:2346:2: rule__AssignmentList__Group__3__Impl
            {
            pushFollow(FOLLOW_2);
            rule__AssignmentList__Group__3__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AssignmentList__Group__3"


    // $ANTLR start "rule__AssignmentList__Group__3__Impl"
    // InternalTyphonDL.g:2352:1: rule__AssignmentList__Group__3__Impl : ( '}' ) ;
    public final void rule__AssignmentList__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2356:1: ( ( '}' ) )
            // InternalTyphonDL.g:2357:1: ( '}' )
            {
            // InternalTyphonDL.g:2357:1: ( '}' )
            // InternalTyphonDL.g:2358:2: '}'
            {
             before(grammarAccess.getAssignmentListAccess().getRightCurlyBracketKeyword_3()); 
            match(input,21,FOLLOW_2); 
             after(grammarAccess.getAssignmentListAccess().getRightCurlyBracketKeyword_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AssignmentList__Group__3__Impl"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group__0"
    // InternalTyphonDL.g:2368:1: rule__CommaSeparatedAssignmentList__Group__0 : rule__CommaSeparatedAssignmentList__Group__0__Impl rule__CommaSeparatedAssignmentList__Group__1 ;
    public final void rule__CommaSeparatedAssignmentList__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2372:1: ( rule__CommaSeparatedAssignmentList__Group__0__Impl rule__CommaSeparatedAssignmentList__Group__1 )
            // InternalTyphonDL.g:2373:2: rule__CommaSeparatedAssignmentList__Group__0__Impl rule__CommaSeparatedAssignmentList__Group__1
            {
            pushFollow(FOLLOW_25);
            rule__CommaSeparatedAssignmentList__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__CommaSeparatedAssignmentList__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group__0"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group__0__Impl"
    // InternalTyphonDL.g:2380:1: rule__CommaSeparatedAssignmentList__Group__0__Impl : ( ( rule__CommaSeparatedAssignmentList__NameAssignment_0 ) ) ;
    public final void rule__CommaSeparatedAssignmentList__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2384:1: ( ( ( rule__CommaSeparatedAssignmentList__NameAssignment_0 ) ) )
            // InternalTyphonDL.g:2385:1: ( ( rule__CommaSeparatedAssignmentList__NameAssignment_0 ) )
            {
            // InternalTyphonDL.g:2385:1: ( ( rule__CommaSeparatedAssignmentList__NameAssignment_0 ) )
            // InternalTyphonDL.g:2386:2: ( rule__CommaSeparatedAssignmentList__NameAssignment_0 )
            {
             before(grammarAccess.getCommaSeparatedAssignmentListAccess().getNameAssignment_0()); 
            // InternalTyphonDL.g:2387:2: ( rule__CommaSeparatedAssignmentList__NameAssignment_0 )
            // InternalTyphonDL.g:2387:3: rule__CommaSeparatedAssignmentList__NameAssignment_0
            {
            pushFollow(FOLLOW_2);
            rule__CommaSeparatedAssignmentList__NameAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getCommaSeparatedAssignmentListAccess().getNameAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group__0__Impl"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group__1"
    // InternalTyphonDL.g:2395:1: rule__CommaSeparatedAssignmentList__Group__1 : rule__CommaSeparatedAssignmentList__Group__1__Impl rule__CommaSeparatedAssignmentList__Group__2 ;
    public final void rule__CommaSeparatedAssignmentList__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2399:1: ( rule__CommaSeparatedAssignmentList__Group__1__Impl rule__CommaSeparatedAssignmentList__Group__2 )
            // InternalTyphonDL.g:2400:2: rule__CommaSeparatedAssignmentList__Group__1__Impl rule__CommaSeparatedAssignmentList__Group__2
            {
            pushFollow(FOLLOW_26);
            rule__CommaSeparatedAssignmentList__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__CommaSeparatedAssignmentList__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group__1"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group__1__Impl"
    // InternalTyphonDL.g:2407:1: rule__CommaSeparatedAssignmentList__Group__1__Impl : ( '[' ) ;
    public final void rule__CommaSeparatedAssignmentList__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2411:1: ( ( '[' ) )
            // InternalTyphonDL.g:2412:1: ( '[' )
            {
            // InternalTyphonDL.g:2412:1: ( '[' )
            // InternalTyphonDL.g:2413:2: '['
            {
             before(grammarAccess.getCommaSeparatedAssignmentListAccess().getLeftSquareBracketKeyword_1()); 
            match(input,30,FOLLOW_2); 
             after(grammarAccess.getCommaSeparatedAssignmentListAccess().getLeftSquareBracketKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group__1__Impl"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group__2"
    // InternalTyphonDL.g:2422:1: rule__CommaSeparatedAssignmentList__Group__2 : rule__CommaSeparatedAssignmentList__Group__2__Impl rule__CommaSeparatedAssignmentList__Group__3 ;
    public final void rule__CommaSeparatedAssignmentList__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2426:1: ( rule__CommaSeparatedAssignmentList__Group__2__Impl rule__CommaSeparatedAssignmentList__Group__3 )
            // InternalTyphonDL.g:2427:2: rule__CommaSeparatedAssignmentList__Group__2__Impl rule__CommaSeparatedAssignmentList__Group__3
            {
            pushFollow(FOLLOW_27);
            rule__CommaSeparatedAssignmentList__Group__2__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__CommaSeparatedAssignmentList__Group__3();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group__2"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group__2__Impl"
    // InternalTyphonDL.g:2434:1: rule__CommaSeparatedAssignmentList__Group__2__Impl : ( ( rule__CommaSeparatedAssignmentList__ValueAssignment_2 ) ) ;
    public final void rule__CommaSeparatedAssignmentList__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2438:1: ( ( ( rule__CommaSeparatedAssignmentList__ValueAssignment_2 ) ) )
            // InternalTyphonDL.g:2439:1: ( ( rule__CommaSeparatedAssignmentList__ValueAssignment_2 ) )
            {
            // InternalTyphonDL.g:2439:1: ( ( rule__CommaSeparatedAssignmentList__ValueAssignment_2 ) )
            // InternalTyphonDL.g:2440:2: ( rule__CommaSeparatedAssignmentList__ValueAssignment_2 )
            {
             before(grammarAccess.getCommaSeparatedAssignmentListAccess().getValueAssignment_2()); 
            // InternalTyphonDL.g:2441:2: ( rule__CommaSeparatedAssignmentList__ValueAssignment_2 )
            // InternalTyphonDL.g:2441:3: rule__CommaSeparatedAssignmentList__ValueAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__CommaSeparatedAssignmentList__ValueAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getCommaSeparatedAssignmentListAccess().getValueAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group__2__Impl"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group__3"
    // InternalTyphonDL.g:2449:1: rule__CommaSeparatedAssignmentList__Group__3 : rule__CommaSeparatedAssignmentList__Group__3__Impl rule__CommaSeparatedAssignmentList__Group__4 ;
    public final void rule__CommaSeparatedAssignmentList__Group__3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2453:1: ( rule__CommaSeparatedAssignmentList__Group__3__Impl rule__CommaSeparatedAssignmentList__Group__4 )
            // InternalTyphonDL.g:2454:2: rule__CommaSeparatedAssignmentList__Group__3__Impl rule__CommaSeparatedAssignmentList__Group__4
            {
            pushFollow(FOLLOW_27);
            rule__CommaSeparatedAssignmentList__Group__3__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__CommaSeparatedAssignmentList__Group__4();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group__3"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group__3__Impl"
    // InternalTyphonDL.g:2461:1: rule__CommaSeparatedAssignmentList__Group__3__Impl : ( ( rule__CommaSeparatedAssignmentList__Group_3__0 )* ) ;
    public final void rule__CommaSeparatedAssignmentList__Group__3__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2465:1: ( ( ( rule__CommaSeparatedAssignmentList__Group_3__0 )* ) )
            // InternalTyphonDL.g:2466:1: ( ( rule__CommaSeparatedAssignmentList__Group_3__0 )* )
            {
            // InternalTyphonDL.g:2466:1: ( ( rule__CommaSeparatedAssignmentList__Group_3__0 )* )
            // InternalTyphonDL.g:2467:2: ( rule__CommaSeparatedAssignmentList__Group_3__0 )*
            {
             before(grammarAccess.getCommaSeparatedAssignmentListAccess().getGroup_3()); 
            // InternalTyphonDL.g:2468:2: ( rule__CommaSeparatedAssignmentList__Group_3__0 )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==32) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // InternalTyphonDL.g:2468:3: rule__CommaSeparatedAssignmentList__Group_3__0
            	    {
            	    pushFollow(FOLLOW_28);
            	    rule__CommaSeparatedAssignmentList__Group_3__0();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

             after(grammarAccess.getCommaSeparatedAssignmentListAccess().getGroup_3()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group__3__Impl"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group__4"
    // InternalTyphonDL.g:2476:1: rule__CommaSeparatedAssignmentList__Group__4 : rule__CommaSeparatedAssignmentList__Group__4__Impl ;
    public final void rule__CommaSeparatedAssignmentList__Group__4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2480:1: ( rule__CommaSeparatedAssignmentList__Group__4__Impl )
            // InternalTyphonDL.g:2481:2: rule__CommaSeparatedAssignmentList__Group__4__Impl
            {
            pushFollow(FOLLOW_2);
            rule__CommaSeparatedAssignmentList__Group__4__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group__4"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group__4__Impl"
    // InternalTyphonDL.g:2487:1: rule__CommaSeparatedAssignmentList__Group__4__Impl : ( ']' ) ;
    public final void rule__CommaSeparatedAssignmentList__Group__4__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2491:1: ( ( ']' ) )
            // InternalTyphonDL.g:2492:1: ( ']' )
            {
            // InternalTyphonDL.g:2492:1: ( ']' )
            // InternalTyphonDL.g:2493:2: ']'
            {
             before(grammarAccess.getCommaSeparatedAssignmentListAccess().getRightSquareBracketKeyword_4()); 
            match(input,31,FOLLOW_2); 
             after(grammarAccess.getCommaSeparatedAssignmentListAccess().getRightSquareBracketKeyword_4()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group__4__Impl"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group_3__0"
    // InternalTyphonDL.g:2503:1: rule__CommaSeparatedAssignmentList__Group_3__0 : rule__CommaSeparatedAssignmentList__Group_3__0__Impl rule__CommaSeparatedAssignmentList__Group_3__1 ;
    public final void rule__CommaSeparatedAssignmentList__Group_3__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2507:1: ( rule__CommaSeparatedAssignmentList__Group_3__0__Impl rule__CommaSeparatedAssignmentList__Group_3__1 )
            // InternalTyphonDL.g:2508:2: rule__CommaSeparatedAssignmentList__Group_3__0__Impl rule__CommaSeparatedAssignmentList__Group_3__1
            {
            pushFollow(FOLLOW_26);
            rule__CommaSeparatedAssignmentList__Group_3__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__CommaSeparatedAssignmentList__Group_3__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group_3__0"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group_3__0__Impl"
    // InternalTyphonDL.g:2515:1: rule__CommaSeparatedAssignmentList__Group_3__0__Impl : ( ',' ) ;
    public final void rule__CommaSeparatedAssignmentList__Group_3__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2519:1: ( ( ',' ) )
            // InternalTyphonDL.g:2520:1: ( ',' )
            {
            // InternalTyphonDL.g:2520:1: ( ',' )
            // InternalTyphonDL.g:2521:2: ','
            {
             before(grammarAccess.getCommaSeparatedAssignmentListAccess().getCommaKeyword_3_0()); 
            match(input,32,FOLLOW_2); 
             after(grammarAccess.getCommaSeparatedAssignmentListAccess().getCommaKeyword_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group_3__0__Impl"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group_3__1"
    // InternalTyphonDL.g:2530:1: rule__CommaSeparatedAssignmentList__Group_3__1 : rule__CommaSeparatedAssignmentList__Group_3__1__Impl ;
    public final void rule__CommaSeparatedAssignmentList__Group_3__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2534:1: ( rule__CommaSeparatedAssignmentList__Group_3__1__Impl )
            // InternalTyphonDL.g:2535:2: rule__CommaSeparatedAssignmentList__Group_3__1__Impl
            {
            pushFollow(FOLLOW_2);
            rule__CommaSeparatedAssignmentList__Group_3__1__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group_3__1"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__Group_3__1__Impl"
    // InternalTyphonDL.g:2541:1: rule__CommaSeparatedAssignmentList__Group_3__1__Impl : ( ( rule__CommaSeparatedAssignmentList__ValuesAssignment_3_1 ) ) ;
    public final void rule__CommaSeparatedAssignmentList__Group_3__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2545:1: ( ( ( rule__CommaSeparatedAssignmentList__ValuesAssignment_3_1 ) ) )
            // InternalTyphonDL.g:2546:1: ( ( rule__CommaSeparatedAssignmentList__ValuesAssignment_3_1 ) )
            {
            // InternalTyphonDL.g:2546:1: ( ( rule__CommaSeparatedAssignmentList__ValuesAssignment_3_1 ) )
            // InternalTyphonDL.g:2547:2: ( rule__CommaSeparatedAssignmentList__ValuesAssignment_3_1 )
            {
             before(grammarAccess.getCommaSeparatedAssignmentListAccess().getValuesAssignment_3_1()); 
            // InternalTyphonDL.g:2548:2: ( rule__CommaSeparatedAssignmentList__ValuesAssignment_3_1 )
            // InternalTyphonDL.g:2548:3: rule__CommaSeparatedAssignmentList__ValuesAssignment_3_1
            {
            pushFollow(FOLLOW_2);
            rule__CommaSeparatedAssignmentList__ValuesAssignment_3_1();

            state._fsp--;


            }

             after(grammarAccess.getCommaSeparatedAssignmentListAccess().getValuesAssignment_3_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__Group_3__1__Impl"


    // $ANTLR start "rule__Assignment__Group__0"
    // InternalTyphonDL.g:2557:1: rule__Assignment__Group__0 : rule__Assignment__Group__0__Impl rule__Assignment__Group__1 ;
    public final void rule__Assignment__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2561:1: ( rule__Assignment__Group__0__Impl rule__Assignment__Group__1 )
            // InternalTyphonDL.g:2562:2: rule__Assignment__Group__0__Impl rule__Assignment__Group__1
            {
            pushFollow(FOLLOW_29);
            rule__Assignment__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Assignment__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Assignment__Group__0"


    // $ANTLR start "rule__Assignment__Group__0__Impl"
    // InternalTyphonDL.g:2569:1: rule__Assignment__Group__0__Impl : ( ( rule__Assignment__NameAssignment_0 ) ) ;
    public final void rule__Assignment__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2573:1: ( ( ( rule__Assignment__NameAssignment_0 ) ) )
            // InternalTyphonDL.g:2574:1: ( ( rule__Assignment__NameAssignment_0 ) )
            {
            // InternalTyphonDL.g:2574:1: ( ( rule__Assignment__NameAssignment_0 ) )
            // InternalTyphonDL.g:2575:2: ( rule__Assignment__NameAssignment_0 )
            {
             before(grammarAccess.getAssignmentAccess().getNameAssignment_0()); 
            // InternalTyphonDL.g:2576:2: ( rule__Assignment__NameAssignment_0 )
            // InternalTyphonDL.g:2576:3: rule__Assignment__NameAssignment_0
            {
            pushFollow(FOLLOW_2);
            rule__Assignment__NameAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getAssignmentAccess().getNameAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Assignment__Group__0__Impl"


    // $ANTLR start "rule__Assignment__Group__1"
    // InternalTyphonDL.g:2584:1: rule__Assignment__Group__1 : rule__Assignment__Group__1__Impl rule__Assignment__Group__2 ;
    public final void rule__Assignment__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2588:1: ( rule__Assignment__Group__1__Impl rule__Assignment__Group__2 )
            // InternalTyphonDL.g:2589:2: rule__Assignment__Group__1__Impl rule__Assignment__Group__2
            {
            pushFollow(FOLLOW_26);
            rule__Assignment__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Assignment__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Assignment__Group__1"


    // $ANTLR start "rule__Assignment__Group__1__Impl"
    // InternalTyphonDL.g:2596:1: rule__Assignment__Group__1__Impl : ( '=' ) ;
    public final void rule__Assignment__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2600:1: ( ( '=' ) )
            // InternalTyphonDL.g:2601:1: ( '=' )
            {
            // InternalTyphonDL.g:2601:1: ( '=' )
            // InternalTyphonDL.g:2602:2: '='
            {
             before(grammarAccess.getAssignmentAccess().getEqualsSignKeyword_1()); 
            match(input,33,FOLLOW_2); 
             after(grammarAccess.getAssignmentAccess().getEqualsSignKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Assignment__Group__1__Impl"


    // $ANTLR start "rule__Assignment__Group__2"
    // InternalTyphonDL.g:2611:1: rule__Assignment__Group__2 : rule__Assignment__Group__2__Impl ;
    public final void rule__Assignment__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2615:1: ( rule__Assignment__Group__2__Impl )
            // InternalTyphonDL.g:2616:2: rule__Assignment__Group__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Assignment__Group__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Assignment__Group__2"


    // $ANTLR start "rule__Assignment__Group__2__Impl"
    // InternalTyphonDL.g:2622:1: rule__Assignment__Group__2__Impl : ( ( rule__Assignment__ValueAssignment_2 ) ) ;
    public final void rule__Assignment__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2626:1: ( ( ( rule__Assignment__ValueAssignment_2 ) ) )
            // InternalTyphonDL.g:2627:1: ( ( rule__Assignment__ValueAssignment_2 ) )
            {
            // InternalTyphonDL.g:2627:1: ( ( rule__Assignment__ValueAssignment_2 ) )
            // InternalTyphonDL.g:2628:2: ( rule__Assignment__ValueAssignment_2 )
            {
             before(grammarAccess.getAssignmentAccess().getValueAssignment_2()); 
            // InternalTyphonDL.g:2629:2: ( rule__Assignment__ValueAssignment_2 )
            // InternalTyphonDL.g:2629:3: rule__Assignment__ValueAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__Assignment__ValueAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getAssignmentAccess().getValueAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Assignment__Group__2__Impl"


    // $ANTLR start "rule__Feature__Group__0"
    // InternalTyphonDL.g:2638:1: rule__Feature__Group__0 : rule__Feature__Group__0__Impl rule__Feature__Group__1 ;
    public final void rule__Feature__Group__0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2642:1: ( rule__Feature__Group__0__Impl rule__Feature__Group__1 )
            // InternalTyphonDL.g:2643:2: rule__Feature__Group__0__Impl rule__Feature__Group__1
            {
            pushFollow(FOLLOW_7);
            rule__Feature__Group__0__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Feature__Group__1();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Feature__Group__0"


    // $ANTLR start "rule__Feature__Group__0__Impl"
    // InternalTyphonDL.g:2650:1: rule__Feature__Group__0__Impl : ( ( rule__Feature__NameAssignment_0 ) ) ;
    public final void rule__Feature__Group__0__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2654:1: ( ( ( rule__Feature__NameAssignment_0 ) ) )
            // InternalTyphonDL.g:2655:1: ( ( rule__Feature__NameAssignment_0 ) )
            {
            // InternalTyphonDL.g:2655:1: ( ( rule__Feature__NameAssignment_0 ) )
            // InternalTyphonDL.g:2656:2: ( rule__Feature__NameAssignment_0 )
            {
             before(grammarAccess.getFeatureAccess().getNameAssignment_0()); 
            // InternalTyphonDL.g:2657:2: ( rule__Feature__NameAssignment_0 )
            // InternalTyphonDL.g:2657:3: rule__Feature__NameAssignment_0
            {
            pushFollow(FOLLOW_2);
            rule__Feature__NameAssignment_0();

            state._fsp--;


            }

             after(grammarAccess.getFeatureAccess().getNameAssignment_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Feature__Group__0__Impl"


    // $ANTLR start "rule__Feature__Group__1"
    // InternalTyphonDL.g:2665:1: rule__Feature__Group__1 : rule__Feature__Group__1__Impl rule__Feature__Group__2 ;
    public final void rule__Feature__Group__1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2669:1: ( rule__Feature__Group__1__Impl rule__Feature__Group__2 )
            // InternalTyphonDL.g:2670:2: rule__Feature__Group__1__Impl rule__Feature__Group__2
            {
            pushFollow(FOLLOW_5);
            rule__Feature__Group__1__Impl();

            state._fsp--;

            pushFollow(FOLLOW_2);
            rule__Feature__Group__2();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Feature__Group__1"


    // $ANTLR start "rule__Feature__Group__1__Impl"
    // InternalTyphonDL.g:2677:1: rule__Feature__Group__1__Impl : ( ':' ) ;
    public final void rule__Feature__Group__1__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2681:1: ( ( ':' ) )
            // InternalTyphonDL.g:2682:1: ( ':' )
            {
            // InternalTyphonDL.g:2682:1: ( ':' )
            // InternalTyphonDL.g:2683:2: ':'
            {
             before(grammarAccess.getFeatureAccess().getColonKeyword_1()); 
            match(input,13,FOLLOW_2); 
             after(grammarAccess.getFeatureAccess().getColonKeyword_1()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Feature__Group__1__Impl"


    // $ANTLR start "rule__Feature__Group__2"
    // InternalTyphonDL.g:2692:1: rule__Feature__Group__2 : rule__Feature__Group__2__Impl ;
    public final void rule__Feature__Group__2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2696:1: ( rule__Feature__Group__2__Impl )
            // InternalTyphonDL.g:2697:2: rule__Feature__Group__2__Impl
            {
            pushFollow(FOLLOW_2);
            rule__Feature__Group__2__Impl();

            state._fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Feature__Group__2"


    // $ANTLR start "rule__Feature__Group__2__Impl"
    // InternalTyphonDL.g:2703:1: rule__Feature__Group__2__Impl : ( ( rule__Feature__TypeAssignment_2 ) ) ;
    public final void rule__Feature__Group__2__Impl() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2707:1: ( ( ( rule__Feature__TypeAssignment_2 ) ) )
            // InternalTyphonDL.g:2708:1: ( ( rule__Feature__TypeAssignment_2 ) )
            {
            // InternalTyphonDL.g:2708:1: ( ( rule__Feature__TypeAssignment_2 ) )
            // InternalTyphonDL.g:2709:2: ( rule__Feature__TypeAssignment_2 )
            {
             before(grammarAccess.getFeatureAccess().getTypeAssignment_2()); 
            // InternalTyphonDL.g:2710:2: ( rule__Feature__TypeAssignment_2 )
            // InternalTyphonDL.g:2710:3: rule__Feature__TypeAssignment_2
            {
            pushFollow(FOLLOW_2);
            rule__Feature__TypeAssignment_2();

            state._fsp--;


            }

             after(grammarAccess.getFeatureAccess().getTypeAssignment_2()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Feature__Group__2__Impl"


    // $ANTLR start "rule__DeploymentModel__ElementsAssignment"
    // InternalTyphonDL.g:2719:1: rule__DeploymentModel__ElementsAssignment : ( ruleType ) ;
    public final void rule__DeploymentModel__ElementsAssignment() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2723:1: ( ( ruleType ) )
            // InternalTyphonDL.g:2724:2: ( ruleType )
            {
            // InternalTyphonDL.g:2724:2: ( ruleType )
            // InternalTyphonDL.g:2725:3: ruleType
            {
             before(grammarAccess.getDeploymentModelAccess().getElementsTypeParserRuleCall_0()); 
            pushFollow(FOLLOW_2);
            ruleType();

            state._fsp--;

             after(grammarAccess.getDeploymentModelAccess().getElementsTypeParserRuleCall_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DeploymentModel__ElementsAssignment"


    // $ANTLR start "rule__DataType__NameAssignment_1"
    // InternalTyphonDL.g:2734:1: rule__DataType__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__DataType__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2738:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:2739:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:2739:2: ( RULE_ID )
            // InternalTyphonDL.g:2740:3: RULE_ID
            {
             before(grammarAccess.getDataTypeAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getDataTypeAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DataType__NameAssignment_1"


    // $ANTLR start "rule__PlatformType__NameAssignment_1"
    // InternalTyphonDL.g:2749:1: rule__PlatformType__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__PlatformType__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2753:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:2754:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:2754:2: ( RULE_ID )
            // InternalTyphonDL.g:2755:3: RULE_ID
            {
             before(grammarAccess.getPlatformTypeAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getPlatformTypeAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__PlatformType__NameAssignment_1"


    // $ANTLR start "rule__DBType__NameAssignment_1"
    // InternalTyphonDL.g:2764:1: rule__DBType__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__DBType__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2768:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:2769:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:2769:2: ( RULE_ID )
            // InternalTyphonDL.g:2770:3: RULE_ID
            {
             before(grammarAccess.getDBTypeAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getDBTypeAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBType__NameAssignment_1"


    // $ANTLR start "rule__Platform__NameAssignment_2"
    // InternalTyphonDL.g:2779:1: rule__Platform__NameAssignment_2 : ( RULE_ID ) ;
    public final void rule__Platform__NameAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2783:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:2784:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:2784:2: ( RULE_ID )
            // InternalTyphonDL.g:2785:3: RULE_ID
            {
             before(grammarAccess.getPlatformAccess().getNameIDTerminalRuleCall_2_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getPlatformAccess().getNameIDTerminalRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__NameAssignment_2"


    // $ANTLR start "rule__Platform__TypeAssignment_4"
    // InternalTyphonDL.g:2794:1: rule__Platform__TypeAssignment_4 : ( ( RULE_ID ) ) ;
    public final void rule__Platform__TypeAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2798:1: ( ( ( RULE_ID ) ) )
            // InternalTyphonDL.g:2799:2: ( ( RULE_ID ) )
            {
            // InternalTyphonDL.g:2799:2: ( ( RULE_ID ) )
            // InternalTyphonDL.g:2800:3: ( RULE_ID )
            {
             before(grammarAccess.getPlatformAccess().getTypePlatformTypeCrossReference_4_0()); 
            // InternalTyphonDL.g:2801:3: ( RULE_ID )
            // InternalTyphonDL.g:2802:4: RULE_ID
            {
             before(grammarAccess.getPlatformAccess().getTypePlatformTypeIDTerminalRuleCall_4_0_1()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getPlatformAccess().getTypePlatformTypeIDTerminalRuleCall_4_0_1()); 

            }

             after(grammarAccess.getPlatformAccess().getTypePlatformTypeCrossReference_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__TypeAssignment_4"


    // $ANTLR start "rule__Platform__ClustersAssignment_6"
    // InternalTyphonDL.g:2813:1: rule__Platform__ClustersAssignment_6 : ( ruleCluster ) ;
    public final void rule__Platform__ClustersAssignment_6() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2817:1: ( ( ruleCluster ) )
            // InternalTyphonDL.g:2818:2: ( ruleCluster )
            {
            // InternalTyphonDL.g:2818:2: ( ruleCluster )
            // InternalTyphonDL.g:2819:3: ruleCluster
            {
             before(grammarAccess.getPlatformAccess().getClustersClusterParserRuleCall_6_0()); 
            pushFollow(FOLLOW_2);
            ruleCluster();

            state._fsp--;

             after(grammarAccess.getPlatformAccess().getClustersClusterParserRuleCall_6_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Platform__ClustersAssignment_6"


    // $ANTLR start "rule__Cluster__NameAssignment_2"
    // InternalTyphonDL.g:2828:1: rule__Cluster__NameAssignment_2 : ( RULE_ID ) ;
    public final void rule__Cluster__NameAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2832:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:2833:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:2833:2: ( RULE_ID )
            // InternalTyphonDL.g:2834:3: RULE_ID
            {
             before(grammarAccess.getClusterAccess().getNameIDTerminalRuleCall_2_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getClusterAccess().getNameIDTerminalRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__NameAssignment_2"


    // $ANTLR start "rule__Cluster__ApplicationsAssignment_4"
    // InternalTyphonDL.g:2843:1: rule__Cluster__ApplicationsAssignment_4 : ( ruleApplication ) ;
    public final void rule__Cluster__ApplicationsAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2847:1: ( ( ruleApplication ) )
            // InternalTyphonDL.g:2848:2: ( ruleApplication )
            {
            // InternalTyphonDL.g:2848:2: ( ruleApplication )
            // InternalTyphonDL.g:2849:3: ruleApplication
            {
             before(grammarAccess.getClusterAccess().getApplicationsApplicationParserRuleCall_4_0()); 
            pushFollow(FOLLOW_2);
            ruleApplication();

            state._fsp--;

             after(grammarAccess.getClusterAccess().getApplicationsApplicationParserRuleCall_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Cluster__ApplicationsAssignment_4"


    // $ANTLR start "rule__Application__NameAssignment_2"
    // InternalTyphonDL.g:2858:1: rule__Application__NameAssignment_2 : ( RULE_ID ) ;
    public final void rule__Application__NameAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2862:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:2863:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:2863:2: ( RULE_ID )
            // InternalTyphonDL.g:2864:3: RULE_ID
            {
             before(grammarAccess.getApplicationAccess().getNameIDTerminalRuleCall_2_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getApplicationAccess().getNameIDTerminalRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__NameAssignment_2"


    // $ANTLR start "rule__Application__ContainersAssignment_4"
    // InternalTyphonDL.g:2873:1: rule__Application__ContainersAssignment_4 : ( ruleContainer ) ;
    public final void rule__Application__ContainersAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2877:1: ( ( ruleContainer ) )
            // InternalTyphonDL.g:2878:2: ( ruleContainer )
            {
            // InternalTyphonDL.g:2878:2: ( ruleContainer )
            // InternalTyphonDL.g:2879:3: ruleContainer
            {
             before(grammarAccess.getApplicationAccess().getContainersContainerParserRuleCall_4_0()); 
            pushFollow(FOLLOW_2);
            ruleContainer();

            state._fsp--;

             after(grammarAccess.getApplicationAccess().getContainersContainerParserRuleCall_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Application__ContainersAssignment_4"


    // $ANTLR start "rule__Container__NameAssignment_2"
    // InternalTyphonDL.g:2888:1: rule__Container__NameAssignment_2 : ( RULE_ID ) ;
    public final void rule__Container__NameAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2892:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:2893:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:2893:2: ( RULE_ID )
            // InternalTyphonDL.g:2894:3: RULE_ID
            {
             before(grammarAccess.getContainerAccess().getNameIDTerminalRuleCall_2_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getContainerAccess().getNameIDTerminalRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__NameAssignment_2"


    // $ANTLR start "rule__Container__PropertiesAssignment_4_0"
    // InternalTyphonDL.g:2903:1: rule__Container__PropertiesAssignment_4_0 : ( ruleProperty ) ;
    public final void rule__Container__PropertiesAssignment_4_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2907:1: ( ( ruleProperty ) )
            // InternalTyphonDL.g:2908:2: ( ruleProperty )
            {
            // InternalTyphonDL.g:2908:2: ( ruleProperty )
            // InternalTyphonDL.g:2909:3: ruleProperty
            {
             before(grammarAccess.getContainerAccess().getPropertiesPropertyParserRuleCall_4_0_0()); 
            pushFollow(FOLLOW_2);
            ruleProperty();

            state._fsp--;

             after(grammarAccess.getContainerAccess().getPropertiesPropertyParserRuleCall_4_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__PropertiesAssignment_4_0"


    // $ANTLR start "rule__Container__FeaturesAssignment_4_1"
    // InternalTyphonDL.g:2918:1: rule__Container__FeaturesAssignment_4_1 : ( ruleFeature ) ;
    public final void rule__Container__FeaturesAssignment_4_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2922:1: ( ( ruleFeature ) )
            // InternalTyphonDL.g:2923:2: ( ruleFeature )
            {
            // InternalTyphonDL.g:2923:2: ( ruleFeature )
            // InternalTyphonDL.g:2924:3: ruleFeature
            {
             before(grammarAccess.getContainerAccess().getFeaturesFeatureParserRuleCall_4_1_0()); 
            pushFollow(FOLLOW_2);
            ruleFeature();

            state._fsp--;

             after(grammarAccess.getContainerAccess().getFeaturesFeatureParserRuleCall_4_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Container__FeaturesAssignment_4_1"


    // $ANTLR start "rule__DBService__NameAssignment_1"
    // InternalTyphonDL.g:2933:1: rule__DBService__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__DBService__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2937:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:2938:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:2938:2: ( RULE_ID )
            // InternalTyphonDL.g:2939:3: RULE_ID
            {
             before(grammarAccess.getDBServiceAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getDBServiceAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBService__NameAssignment_1"


    // $ANTLR start "rule__DBService__FeaturesAssignment_3"
    // InternalTyphonDL.g:2948:1: rule__DBService__FeaturesAssignment_3 : ( ruleFeature ) ;
    public final void rule__DBService__FeaturesAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2952:1: ( ( ruleFeature ) )
            // InternalTyphonDL.g:2953:2: ( ruleFeature )
            {
            // InternalTyphonDL.g:2953:2: ( ruleFeature )
            // InternalTyphonDL.g:2954:3: ruleFeature
            {
             before(grammarAccess.getDBServiceAccess().getFeaturesFeatureParserRuleCall_3_0()); 
            pushFollow(FOLLOW_2);
            ruleFeature();

            state._fsp--;

             after(grammarAccess.getDBServiceAccess().getFeaturesFeatureParserRuleCall_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__DBService__FeaturesAssignment_3"


    // $ANTLR start "rule__BusinessService__NameAssignment_1"
    // InternalTyphonDL.g:2963:1: rule__BusinessService__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__BusinessService__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2967:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:2968:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:2968:2: ( RULE_ID )
            // InternalTyphonDL.g:2969:3: RULE_ID
            {
             before(grammarAccess.getBusinessServiceAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getBusinessServiceAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__BusinessService__NameAssignment_1"


    // $ANTLR start "rule__BusinessService__FeaturesAssignment_3"
    // InternalTyphonDL.g:2978:1: rule__BusinessService__FeaturesAssignment_3 : ( ruleFeature ) ;
    public final void rule__BusinessService__FeaturesAssignment_3() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2982:1: ( ( ruleFeature ) )
            // InternalTyphonDL.g:2983:2: ( ruleFeature )
            {
            // InternalTyphonDL.g:2983:2: ( ruleFeature )
            // InternalTyphonDL.g:2984:3: ruleFeature
            {
             before(grammarAccess.getBusinessServiceAccess().getFeaturesFeatureParserRuleCall_3_0()); 
            pushFollow(FOLLOW_2);
            ruleFeature();

            state._fsp--;

             after(grammarAccess.getBusinessServiceAccess().getFeaturesFeatureParserRuleCall_3_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__BusinessService__FeaturesAssignment_3"


    // $ANTLR start "rule__Entity__NameAssignment_1"
    // InternalTyphonDL.g:2993:1: rule__Entity__NameAssignment_1 : ( RULE_ID ) ;
    public final void rule__Entity__NameAssignment_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:2997:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:2998:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:2998:2: ( RULE_ID )
            // InternalTyphonDL.g:2999:3: RULE_ID
            {
             before(grammarAccess.getEntityAccess().getNameIDTerminalRuleCall_1_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getEntityAccess().getNameIDTerminalRuleCall_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__NameAssignment_1"


    // $ANTLR start "rule__Entity__SuperTypeAssignment_2_1"
    // InternalTyphonDL.g:3008:1: rule__Entity__SuperTypeAssignment_2_1 : ( ( RULE_ID ) ) ;
    public final void rule__Entity__SuperTypeAssignment_2_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:3012:1: ( ( ( RULE_ID ) ) )
            // InternalTyphonDL.g:3013:2: ( ( RULE_ID ) )
            {
            // InternalTyphonDL.g:3013:2: ( ( RULE_ID ) )
            // InternalTyphonDL.g:3014:3: ( RULE_ID )
            {
             before(grammarAccess.getEntityAccess().getSuperTypeEntityCrossReference_2_1_0()); 
            // InternalTyphonDL.g:3015:3: ( RULE_ID )
            // InternalTyphonDL.g:3016:4: RULE_ID
            {
             before(grammarAccess.getEntityAccess().getSuperTypeEntityIDTerminalRuleCall_2_1_0_1()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getEntityAccess().getSuperTypeEntityIDTerminalRuleCall_2_1_0_1()); 

            }

             after(grammarAccess.getEntityAccess().getSuperTypeEntityCrossReference_2_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__SuperTypeAssignment_2_1"


    // $ANTLR start "rule__Entity__FeaturesAssignment_4"
    // InternalTyphonDL.g:3027:1: rule__Entity__FeaturesAssignment_4 : ( ruleFeature ) ;
    public final void rule__Entity__FeaturesAssignment_4() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:3031:1: ( ( ruleFeature ) )
            // InternalTyphonDL.g:3032:2: ( ruleFeature )
            {
            // InternalTyphonDL.g:3032:2: ( ruleFeature )
            // InternalTyphonDL.g:3033:3: ruleFeature
            {
             before(grammarAccess.getEntityAccess().getFeaturesFeatureParserRuleCall_4_0()); 
            pushFollow(FOLLOW_2);
            ruleFeature();

            state._fsp--;

             after(grammarAccess.getEntityAccess().getFeaturesFeatureParserRuleCall_4_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Entity__FeaturesAssignment_4"


    // $ANTLR start "rule__EnvList__EnvironmentVarsAssignment_2"
    // InternalTyphonDL.g:3042:1: rule__EnvList__EnvironmentVarsAssignment_2 : ( RULE_MYSTRING ) ;
    public final void rule__EnvList__EnvironmentVarsAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:3046:1: ( ( RULE_MYSTRING ) )
            // InternalTyphonDL.g:3047:2: ( RULE_MYSTRING )
            {
            // InternalTyphonDL.g:3047:2: ( RULE_MYSTRING )
            // InternalTyphonDL.g:3048:3: RULE_MYSTRING
            {
             before(grammarAccess.getEnvListAccess().getEnvironmentVarsMYSTRINGTerminalRuleCall_2_0()); 
            match(input,RULE_MYSTRING,FOLLOW_2); 
             after(grammarAccess.getEnvListAccess().getEnvironmentVarsMYSTRINGTerminalRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__EnvList__EnvironmentVarsAssignment_2"


    // $ANTLR start "rule__AssignmentList__NameAssignment_0"
    // InternalTyphonDL.g:3057:1: rule__AssignmentList__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__AssignmentList__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:3061:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:3062:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:3062:2: ( RULE_ID )
            // InternalTyphonDL.g:3063:3: RULE_ID
            {
             before(grammarAccess.getAssignmentListAccess().getNameIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getAssignmentListAccess().getNameIDTerminalRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AssignmentList__NameAssignment_0"


    // $ANTLR start "rule__AssignmentList__AssignmentsAssignment_2"
    // InternalTyphonDL.g:3072:1: rule__AssignmentList__AssignmentsAssignment_2 : ( ruleAssignment ) ;
    public final void rule__AssignmentList__AssignmentsAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:3076:1: ( ( ruleAssignment ) )
            // InternalTyphonDL.g:3077:2: ( ruleAssignment )
            {
            // InternalTyphonDL.g:3077:2: ( ruleAssignment )
            // InternalTyphonDL.g:3078:3: ruleAssignment
            {
             before(grammarAccess.getAssignmentListAccess().getAssignmentsAssignmentParserRuleCall_2_0()); 
            pushFollow(FOLLOW_2);
            ruleAssignment();

            state._fsp--;

             after(grammarAccess.getAssignmentListAccess().getAssignmentsAssignmentParserRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__AssignmentList__AssignmentsAssignment_2"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__NameAssignment_0"
    // InternalTyphonDL.g:3087:1: rule__CommaSeparatedAssignmentList__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__CommaSeparatedAssignmentList__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:3091:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:3092:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:3092:2: ( RULE_ID )
            // InternalTyphonDL.g:3093:3: RULE_ID
            {
             before(grammarAccess.getCommaSeparatedAssignmentListAccess().getNameIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getCommaSeparatedAssignmentListAccess().getNameIDTerminalRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__NameAssignment_0"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__ValueAssignment_2"
    // InternalTyphonDL.g:3102:1: rule__CommaSeparatedAssignmentList__ValueAssignment_2 : ( ruleValue ) ;
    public final void rule__CommaSeparatedAssignmentList__ValueAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:3106:1: ( ( ruleValue ) )
            // InternalTyphonDL.g:3107:2: ( ruleValue )
            {
            // InternalTyphonDL.g:3107:2: ( ruleValue )
            // InternalTyphonDL.g:3108:3: ruleValue
            {
             before(grammarAccess.getCommaSeparatedAssignmentListAccess().getValueValueParserRuleCall_2_0()); 
            pushFollow(FOLLOW_2);
            ruleValue();

            state._fsp--;

             after(grammarAccess.getCommaSeparatedAssignmentListAccess().getValueValueParserRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__ValueAssignment_2"


    // $ANTLR start "rule__CommaSeparatedAssignmentList__ValuesAssignment_3_1"
    // InternalTyphonDL.g:3117:1: rule__CommaSeparatedAssignmentList__ValuesAssignment_3_1 : ( ruleValue ) ;
    public final void rule__CommaSeparatedAssignmentList__ValuesAssignment_3_1() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:3121:1: ( ( ruleValue ) )
            // InternalTyphonDL.g:3122:2: ( ruleValue )
            {
            // InternalTyphonDL.g:3122:2: ( ruleValue )
            // InternalTyphonDL.g:3123:3: ruleValue
            {
             before(grammarAccess.getCommaSeparatedAssignmentListAccess().getValuesValueParserRuleCall_3_1_0()); 
            pushFollow(FOLLOW_2);
            ruleValue();

            state._fsp--;

             after(grammarAccess.getCommaSeparatedAssignmentListAccess().getValuesValueParserRuleCall_3_1_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__CommaSeparatedAssignmentList__ValuesAssignment_3_1"


    // $ANTLR start "rule__Assignment__NameAssignment_0"
    // InternalTyphonDL.g:3132:1: rule__Assignment__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__Assignment__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:3136:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:3137:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:3137:2: ( RULE_ID )
            // InternalTyphonDL.g:3138:3: RULE_ID
            {
             before(grammarAccess.getAssignmentAccess().getNameIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getAssignmentAccess().getNameIDTerminalRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Assignment__NameAssignment_0"


    // $ANTLR start "rule__Assignment__ValueAssignment_2"
    // InternalTyphonDL.g:3147:1: rule__Assignment__ValueAssignment_2 : ( ruleValue ) ;
    public final void rule__Assignment__ValueAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:3151:1: ( ( ruleValue ) )
            // InternalTyphonDL.g:3152:2: ( ruleValue )
            {
            // InternalTyphonDL.g:3152:2: ( ruleValue )
            // InternalTyphonDL.g:3153:3: ruleValue
            {
             before(grammarAccess.getAssignmentAccess().getValueValueParserRuleCall_2_0()); 
            pushFollow(FOLLOW_2);
            ruleValue();

            state._fsp--;

             after(grammarAccess.getAssignmentAccess().getValueValueParserRuleCall_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Assignment__ValueAssignment_2"


    // $ANTLR start "rule__Feature__NameAssignment_0"
    // InternalTyphonDL.g:3162:1: rule__Feature__NameAssignment_0 : ( RULE_ID ) ;
    public final void rule__Feature__NameAssignment_0() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:3166:1: ( ( RULE_ID ) )
            // InternalTyphonDL.g:3167:2: ( RULE_ID )
            {
            // InternalTyphonDL.g:3167:2: ( RULE_ID )
            // InternalTyphonDL.g:3168:3: RULE_ID
            {
             before(grammarAccess.getFeatureAccess().getNameIDTerminalRuleCall_0_0()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getFeatureAccess().getNameIDTerminalRuleCall_0_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Feature__NameAssignment_0"


    // $ANTLR start "rule__Feature__TypeAssignment_2"
    // InternalTyphonDL.g:3177:1: rule__Feature__TypeAssignment_2 : ( ( RULE_ID ) ) ;
    public final void rule__Feature__TypeAssignment_2() throws RecognitionException {

        		int stackSize = keepStackSize();
        	
        try {
            // InternalTyphonDL.g:3181:1: ( ( ( RULE_ID ) ) )
            // InternalTyphonDL.g:3182:2: ( ( RULE_ID ) )
            {
            // InternalTyphonDL.g:3182:2: ( ( RULE_ID ) )
            // InternalTyphonDL.g:3183:3: ( RULE_ID )
            {
             before(grammarAccess.getFeatureAccess().getTypeTypeCrossReference_2_0()); 
            // InternalTyphonDL.g:3184:3: ( RULE_ID )
            // InternalTyphonDL.g:3185:4: RULE_ID
            {
             before(grammarAccess.getFeatureAccess().getTypeTypeIDTerminalRuleCall_2_0_1()); 
            match(input,RULE_ID,FOLLOW_2); 
             after(grammarAccess.getFeatureAccess().getTypeTypeIDTerminalRuleCall_2_0_1()); 

            }

             after(grammarAccess.getFeatureAccess().getTypeTypeCrossReference_2_0()); 

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {

            	restoreStackSize(stackSize);

        }
        return ;
    }
    // $ANTLR end "rule__Feature__TypeAssignment_2"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x000000000FCF0002L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x000000000000F072L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000600000L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000000A00000L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000001200000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000020200020L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000020000022L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000010100000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x000000000000F070L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0000000180000000L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000200000000L});

}