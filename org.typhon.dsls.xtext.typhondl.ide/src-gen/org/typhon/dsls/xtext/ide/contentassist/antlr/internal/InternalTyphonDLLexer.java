package org.typhon.dsls.xtext.ide.contentassist.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.ide.editor.contentassist.antlr.internal.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalTyphonDLLexer extends Lexer {
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

    public InternalTyphonDLLexer() {;} 
    public InternalTyphonDLLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalTyphonDLLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "InternalTyphonDL.g"; }

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:11:7: ( '/' )
            // InternalTyphonDL.g:11:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:12:7: ( ':' )
            // InternalTyphonDL.g:12:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:13:7: ( '-' )
            // InternalTyphonDL.g:13:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:14:7: ( '.' )
            // InternalTyphonDL.g:14:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:15:7: ( 'datatype' )
            // InternalTyphonDL.g:15:9: 'datatype'
            {
            match("datatype"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:16:7: ( 'platformtype' )
            // InternalTyphonDL.g:16:9: 'platformtype'
            {
            match("platformtype"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:17:7: ( 'dbtype' )
            // InternalTyphonDL.g:17:9: 'dbtype'
            {
            match("dbtype"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:18:7: ( 'platform' )
            // InternalTyphonDL.g:18:9: 'platform'
            {
            match("platform"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:19:7: ( '{' )
            // InternalTyphonDL.g:19:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:20:7: ( '}' )
            // InternalTyphonDL.g:20:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:21:7: ( 'cluster' )
            // InternalTyphonDL.g:21:9: 'cluster'
            {
            match("cluster"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:22:7: ( 'application' )
            // InternalTyphonDL.g:22:9: 'application'
            {
            match("application"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:23:7: ( 'container' )
            // InternalTyphonDL.g:23:9: 'container'
            {
            match("container"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:24:7: ( 'dbService' )
            // InternalTyphonDL.g:24:9: 'dbService'
            {
            match("dbService"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:25:7: ( 'businessService' )
            // InternalTyphonDL.g:25:9: 'businessService'
            {
            match("businessService"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:26:7: ( 'entity' )
            // InternalTyphonDL.g:26:9: 'entity'
            {
            match("entity"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:27:7: ( 'extends' )
            // InternalTyphonDL.g:27:9: 'extends'
            {
            match("extends"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:28:7: ( 'environment' )
            // InternalTyphonDL.g:28:9: 'environment'
            {
            match("environment"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:29:7: ( '[' )
            // InternalTyphonDL.g:29:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:30:7: ( ']' )
            // InternalTyphonDL.g:30:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:31:7: ( ',' )
            // InternalTyphonDL.g:31:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:32:7: ( '=' )
            // InternalTyphonDL.g:32:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "RULE_MYSTRING"
    public final void mRULE_MYSTRING() throws RecognitionException {
        try {
            int _type = RULE_MYSTRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:3196:15: ( '\"' 'A' .. 'Z' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' | '=' )* '\"' )
            // InternalTyphonDL.g:3196:17: '\"' 'A' .. 'Z' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' | '=' )* '\"'
            {
            match('\"'); 
            matchRange('A','Z'); 
            // InternalTyphonDL.g:3196:30: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' | '=' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')||LA1_0=='='||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalTyphonDL.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||input.LA(1)=='='||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_MYSTRING"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:3198:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // InternalTyphonDL.g:3198:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            // InternalTyphonDL.g:3198:11: ( '^' )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='^') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // InternalTyphonDL.g:3198:11: '^'
                    {
                    match('^'); 

                    }
                    break;

            }

            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // InternalTyphonDL.g:3198:40: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalTyphonDL.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ID"

    // $ANTLR start "RULE_INT"
    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:3200:10: ( ( '0' .. '9' )+ )
            // InternalTyphonDL.g:3200:12: ( '0' .. '9' )+
            {
            // InternalTyphonDL.g:3200:12: ( '0' .. '9' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // InternalTyphonDL.g:3200:13: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_INT"

    // $ANTLR start "RULE_STRING"
    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:3202:13: ( ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' ) )
            // InternalTyphonDL.g:3202:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            {
            // InternalTyphonDL.g:3202:15: ( '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"' | '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='\"') ) {
                alt7=1;
            }
            else if ( (LA7_0=='\'') ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // InternalTyphonDL.g:3202:16: '\"' ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
                    {
                    match('\"'); 
                    // InternalTyphonDL.g:3202:20: ( '\\\\' . | ~ ( ( '\\\\' | '\"' ) ) )*
                    loop5:
                    do {
                        int alt5=3;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0=='\\') ) {
                            alt5=1;
                        }
                        else if ( ((LA5_0>='\u0000' && LA5_0<='!')||(LA5_0>='#' && LA5_0<='[')||(LA5_0>=']' && LA5_0<='\uFFFF')) ) {
                            alt5=2;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // InternalTyphonDL.g:3202:21: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalTyphonDL.g:3202:28: ~ ( ( '\\\\' | '\"' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // InternalTyphonDL.g:3202:48: '\\'' ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
                    {
                    match('\''); 
                    // InternalTyphonDL.g:3202:53: ( '\\\\' . | ~ ( ( '\\\\' | '\\'' ) ) )*
                    loop6:
                    do {
                        int alt6=3;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0=='\\') ) {
                            alt6=1;
                        }
                        else if ( ((LA6_0>='\u0000' && LA6_0<='&')||(LA6_0>='(' && LA6_0<='[')||(LA6_0>=']' && LA6_0<='\uFFFF')) ) {
                            alt6=2;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // InternalTyphonDL.g:3202:54: '\\\\' .
                    	    {
                    	    match('\\'); 
                    	    matchAny(); 

                    	    }
                    	    break;
                    	case 2 :
                    	    // InternalTyphonDL.g:3202:61: ~ ( ( '\\\\' | '\\'' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);

                    match('\''); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_STRING"

    // $ANTLR start "RULE_ML_COMMENT"
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:3204:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // InternalTyphonDL.g:3204:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // InternalTyphonDL.g:3204:24: ( options {greedy=false; } : . )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='*') ) {
                    int LA8_1 = input.LA(2);

                    if ( (LA8_1=='/') ) {
                        alt8=2;
                    }
                    else if ( ((LA8_1>='\u0000' && LA8_1<='.')||(LA8_1>='0' && LA8_1<='\uFFFF')) ) {
                        alt8=1;
                    }


                }
                else if ( ((LA8_0>='\u0000' && LA8_0<=')')||(LA8_0>='+' && LA8_0<='\uFFFF')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // InternalTyphonDL.g:3204:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            match("*/"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ML_COMMENT"

    // $ANTLR start "RULE_SL_COMMENT"
    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:3206:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // InternalTyphonDL.g:3206:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // InternalTyphonDL.g:3206:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='\u0000' && LA9_0<='\t')||(LA9_0>='\u000B' && LA9_0<='\f')||(LA9_0>='\u000E' && LA9_0<='\uFFFF')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalTyphonDL.g:3206:24: ~ ( ( '\\n' | '\\r' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            // InternalTyphonDL.g:3206:40: ( ( '\\r' )? '\\n' )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='\n'||LA11_0=='\r') ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // InternalTyphonDL.g:3206:41: ( '\\r' )? '\\n'
                    {
                    // InternalTyphonDL.g:3206:41: ( '\\r' )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0=='\r') ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // InternalTyphonDL.g:3206:41: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }

                    match('\n'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_SL_COMMENT"

    // $ANTLR start "RULE_WS"
    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:3208:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // InternalTyphonDL.g:3208:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // InternalTyphonDL.g:3208:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt12=0;
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>='\t' && LA12_0<='\n')||LA12_0=='\r'||LA12_0==' ') ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // InternalTyphonDL.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt12 >= 1 ) break loop12;
                        EarlyExitException eee =
                            new EarlyExitException(12, input);
                        throw eee;
                }
                cnt12++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_WS"

    // $ANTLR start "RULE_ANY_OTHER"
    public final void mRULE_ANY_OTHER() throws RecognitionException {
        try {
            int _type = RULE_ANY_OTHER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // InternalTyphonDL.g:3210:16: ( . )
            // InternalTyphonDL.g:3210:18: .
            {
            matchAny(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ANY_OTHER"

    public void mTokens() throws RecognitionException {
        // InternalTyphonDL.g:1:8: ( T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | RULE_MYSTRING | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt13=30;
        alt13 = dfa13.predict(input);
        switch (alt13) {
            case 1 :
                // InternalTyphonDL.g:1:10: T__12
                {
                mT__12(); 

                }
                break;
            case 2 :
                // InternalTyphonDL.g:1:16: T__13
                {
                mT__13(); 

                }
                break;
            case 3 :
                // InternalTyphonDL.g:1:22: T__14
                {
                mT__14(); 

                }
                break;
            case 4 :
                // InternalTyphonDL.g:1:28: T__15
                {
                mT__15(); 

                }
                break;
            case 5 :
                // InternalTyphonDL.g:1:34: T__16
                {
                mT__16(); 

                }
                break;
            case 6 :
                // InternalTyphonDL.g:1:40: T__17
                {
                mT__17(); 

                }
                break;
            case 7 :
                // InternalTyphonDL.g:1:46: T__18
                {
                mT__18(); 

                }
                break;
            case 8 :
                // InternalTyphonDL.g:1:52: T__19
                {
                mT__19(); 

                }
                break;
            case 9 :
                // InternalTyphonDL.g:1:58: T__20
                {
                mT__20(); 

                }
                break;
            case 10 :
                // InternalTyphonDL.g:1:64: T__21
                {
                mT__21(); 

                }
                break;
            case 11 :
                // InternalTyphonDL.g:1:70: T__22
                {
                mT__22(); 

                }
                break;
            case 12 :
                // InternalTyphonDL.g:1:76: T__23
                {
                mT__23(); 

                }
                break;
            case 13 :
                // InternalTyphonDL.g:1:82: T__24
                {
                mT__24(); 

                }
                break;
            case 14 :
                // InternalTyphonDL.g:1:88: T__25
                {
                mT__25(); 

                }
                break;
            case 15 :
                // InternalTyphonDL.g:1:94: T__26
                {
                mT__26(); 

                }
                break;
            case 16 :
                // InternalTyphonDL.g:1:100: T__27
                {
                mT__27(); 

                }
                break;
            case 17 :
                // InternalTyphonDL.g:1:106: T__28
                {
                mT__28(); 

                }
                break;
            case 18 :
                // InternalTyphonDL.g:1:112: T__29
                {
                mT__29(); 

                }
                break;
            case 19 :
                // InternalTyphonDL.g:1:118: T__30
                {
                mT__30(); 

                }
                break;
            case 20 :
                // InternalTyphonDL.g:1:124: T__31
                {
                mT__31(); 

                }
                break;
            case 21 :
                // InternalTyphonDL.g:1:130: T__32
                {
                mT__32(); 

                }
                break;
            case 22 :
                // InternalTyphonDL.g:1:136: T__33
                {
                mT__33(); 

                }
                break;
            case 23 :
                // InternalTyphonDL.g:1:142: RULE_MYSTRING
                {
                mRULE_MYSTRING(); 

                }
                break;
            case 24 :
                // InternalTyphonDL.g:1:156: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 25 :
                // InternalTyphonDL.g:1:164: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 26 :
                // InternalTyphonDL.g:1:173: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 27 :
                // InternalTyphonDL.g:1:185: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 28 :
                // InternalTyphonDL.g:1:201: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 29 :
                // InternalTyphonDL.g:1:217: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 30 :
                // InternalTyphonDL.g:1:225: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


    protected DFA13 dfa13 = new DFA13(this);
    static final String DFA13_eotS =
        "\1\uffff\1\32\3\uffff\2\40\2\uffff\4\40\4\uffff\2\27\2\uffff\1\27\10\uffff\2\40\1\uffff\1\40\2\uffff\6\40\10\uffff\13\40\2\uffff\13\40\1\uffff\14\40\1\142\6\40\1\151\3\40\1\uffff\2\40\1\157\3\40\1\uffff\1\40\1\164\1\165\1\40\1\170\1\uffff\4\40\2\uffff\1\175\1\40\1\uffff\1\177\3\40\1\uffff\1\40\1\uffff\4\40\1\u0088\1\40\1\u008a\1\u008b\1\uffff\1\40\2\uffff\2\40\1\u008f\1\uffff";
    static final String DFA13_eofS =
        "\u0090\uffff";
    static final String DFA13_minS =
        "\1\0\1\52\3\uffff\1\141\1\154\2\uffff\1\154\1\160\1\165\1\156\4\uffff\1\0\1\101\2\uffff\1\0\10\uffff\1\164\1\123\1\uffff\1\141\2\uffff\1\165\1\156\1\160\1\163\2\164\4\uffff\1\0\3\uffff\1\141\1\171\1\145\1\164\1\163\1\164\1\154\3\151\1\145\1\0\1\uffff\1\164\1\160\1\162\1\146\1\164\1\141\1\151\1\156\1\164\1\162\1\156\1\uffff\1\171\1\145\1\166\1\157\1\145\1\151\1\143\1\145\1\171\1\157\1\144\1\160\1\60\1\151\2\162\1\156\1\141\1\163\1\60\1\156\1\163\1\145\1\uffff\1\143\1\155\1\60\1\145\1\164\1\163\1\uffff\1\155\2\60\1\145\1\60\1\uffff\1\162\1\151\1\123\1\145\2\uffff\1\60\1\171\1\uffff\1\60\1\157\1\145\1\156\1\uffff\1\160\1\uffff\1\156\1\162\1\164\1\145\1\60\1\166\2\60\1\uffff\1\151\2\uffff\1\143\1\145\1\60\1\uffff";
    static final String DFA13_maxS =
        "\1\uffff\1\57\3\uffff\1\142\1\154\2\uffff\1\157\1\160\1\165\1\170\4\uffff\1\uffff\1\172\2\uffff\1\uffff\10\uffff\2\164\1\uffff\1\141\2\uffff\1\165\1\156\1\160\1\163\1\166\1\164\4\uffff\1\uffff\3\uffff\1\141\1\171\1\145\1\164\1\163\1\164\1\154\3\151\1\145\1\uffff\1\uffff\1\164\1\160\1\162\1\146\1\164\1\141\1\151\1\156\1\164\1\162\1\156\1\uffff\1\171\1\145\1\166\1\157\1\145\1\151\1\143\1\145\1\171\1\157\1\144\1\160\1\172\1\151\2\162\1\156\1\141\1\163\1\172\1\156\1\163\1\145\1\uffff\1\143\1\155\1\172\1\145\1\164\1\163\1\uffff\1\155\2\172\1\145\1\172\1\uffff\1\162\1\151\1\123\1\145\2\uffff\1\172\1\171\1\uffff\1\172\1\157\1\145\1\156\1\uffff\1\160\1\uffff\1\156\1\162\1\164\1\145\1\172\1\166\2\172\1\uffff\1\151\2\uffff\1\143\1\145\1\172\1\uffff";
    static final String DFA13_acceptS =
        "\2\uffff\1\2\1\3\1\4\2\uffff\1\11\1\12\4\uffff\1\23\1\24\1\25\1\26\2\uffff\1\30\1\31\1\uffff\1\35\1\36\1\33\1\34\1\1\1\2\1\3\1\4\2\uffff\1\30\1\uffff\1\11\1\12\6\uffff\1\23\1\24\1\25\1\26\1\uffff\1\32\1\31\1\35\14\uffff\1\27\13\uffff\1\27\27\uffff\1\7\6\uffff\1\20\5\uffff\1\13\4\uffff\1\21\1\5\2\uffff\1\10\4\uffff\1\16\1\uffff\1\15\10\uffff\1\14\1\uffff\1\22\1\6\3\uffff\1\17";
    static final String DFA13_specialS =
        "\1\2\20\uffff\1\4\3\uffff\1\3\30\uffff\1\0\16\uffff\1\1\122\uffff}>";
    static final String[] DFA13_transitionS = {
            "\11\27\2\26\2\27\1\26\22\27\1\26\1\27\1\21\4\27\1\25\4\27\1\17\1\3\1\4\1\1\12\24\1\2\2\27\1\20\3\27\32\23\1\15\1\27\1\16\1\22\1\23\1\27\1\12\1\13\1\11\1\5\1\14\12\23\1\6\12\23\1\7\1\27\1\10\uff82\27",
            "\1\30\4\uffff\1\31",
            "",
            "",
            "",
            "\1\36\1\37",
            "\1\41",
            "",
            "",
            "\1\44\2\uffff\1\45",
            "\1\46",
            "\1\47",
            "\1\50\11\uffff\1\51",
            "",
            "",
            "",
            "",
            "\101\57\32\56\uffa5\57",
            "\32\40\4\uffff\1\40\1\uffff\32\40",
            "",
            "",
            "\0\57",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\62",
            "\1\64\40\uffff\1\63",
            "",
            "\1\65",
            "",
            "",
            "\1\66",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\72\1\uffff\1\73",
            "\1\74",
            "",
            "",
            "",
            "",
            "\42\57\1\76\15\57\12\75\3\57\1\75\3\57\32\75\4\57\1\75\1\57\32\75\uff85\57",
            "",
            "",
            "",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "\1\103",
            "\1\104",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "\42\57\1\76\15\57\12\75\3\57\1\75\3\57\32\75\4\57\1\75\1\57\32\75\uff85\57",
            "",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\122",
            "\1\123",
            "\1\124",
            "\1\125",
            "",
            "\1\126",
            "\1\127",
            "\1\130",
            "\1\131",
            "\1\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "\1\140",
            "\1\141",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\143",
            "\1\144",
            "\1\145",
            "\1\146",
            "\1\147",
            "\1\150",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\152",
            "\1\153",
            "\1\154",
            "",
            "\1\155",
            "\1\156",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\160",
            "\1\161",
            "\1\162",
            "",
            "\1\163",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\166",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\23\40\1\167\6\40",
            "",
            "\1\171",
            "\1\172",
            "\1\173",
            "\1\174",
            "",
            "",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\176",
            "",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\u0080",
            "\1\u0081",
            "\1\u0082",
            "",
            "\1\u0083",
            "",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "\1\u0087",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\1\u0089",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            "",
            "\1\u008c",
            "",
            "",
            "\1\u008d",
            "\1\u008e",
            "\12\40\7\uffff\32\40\4\uffff\1\40\1\uffff\32\40",
            ""
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | RULE_MYSTRING | RULE_ID | RULE_INT | RULE_STRING | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA13_46 = input.LA(1);

                        s = -1;
                        if ( ((LA13_46>='0' && LA13_46<='9')||LA13_46=='='||(LA13_46>='A' && LA13_46<='Z')||LA13_46=='_'||(LA13_46>='a' && LA13_46<='z')) ) {s = 61;}

                        else if ( (LA13_46=='\"') ) {s = 62;}

                        else if ( ((LA13_46>='\u0000' && LA13_46<='!')||(LA13_46>='#' && LA13_46<='/')||(LA13_46>=':' && LA13_46<='<')||(LA13_46>='>' && LA13_46<='@')||(LA13_46>='[' && LA13_46<='^')||LA13_46=='`'||(LA13_46>='{' && LA13_46<='\uFFFF')) ) {s = 47;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA13_61 = input.LA(1);

                        s = -1;
                        if ( (LA13_61=='\"') ) {s = 62;}

                        else if ( ((LA13_61>='0' && LA13_61<='9')||LA13_61=='='||(LA13_61>='A' && LA13_61<='Z')||LA13_61=='_'||(LA13_61>='a' && LA13_61<='z')) ) {s = 61;}

                        else if ( ((LA13_61>='\u0000' && LA13_61<='!')||(LA13_61>='#' && LA13_61<='/')||(LA13_61>=':' && LA13_61<='<')||(LA13_61>='>' && LA13_61<='@')||(LA13_61>='[' && LA13_61<='^')||LA13_61=='`'||(LA13_61>='{' && LA13_61<='\uFFFF')) ) {s = 47;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA13_0 = input.LA(1);

                        s = -1;
                        if ( (LA13_0=='/') ) {s = 1;}

                        else if ( (LA13_0==':') ) {s = 2;}

                        else if ( (LA13_0=='-') ) {s = 3;}

                        else if ( (LA13_0=='.') ) {s = 4;}

                        else if ( (LA13_0=='d') ) {s = 5;}

                        else if ( (LA13_0=='p') ) {s = 6;}

                        else if ( (LA13_0=='{') ) {s = 7;}

                        else if ( (LA13_0=='}') ) {s = 8;}

                        else if ( (LA13_0=='c') ) {s = 9;}

                        else if ( (LA13_0=='a') ) {s = 10;}

                        else if ( (LA13_0=='b') ) {s = 11;}

                        else if ( (LA13_0=='e') ) {s = 12;}

                        else if ( (LA13_0=='[') ) {s = 13;}

                        else if ( (LA13_0==']') ) {s = 14;}

                        else if ( (LA13_0==',') ) {s = 15;}

                        else if ( (LA13_0=='=') ) {s = 16;}

                        else if ( (LA13_0=='\"') ) {s = 17;}

                        else if ( (LA13_0=='^') ) {s = 18;}

                        else if ( ((LA13_0>='A' && LA13_0<='Z')||LA13_0=='_'||(LA13_0>='f' && LA13_0<='o')||(LA13_0>='q' && LA13_0<='z')) ) {s = 19;}

                        else if ( ((LA13_0>='0' && LA13_0<='9')) ) {s = 20;}

                        else if ( (LA13_0=='\'') ) {s = 21;}

                        else if ( ((LA13_0>='\t' && LA13_0<='\n')||LA13_0=='\r'||LA13_0==' ') ) {s = 22;}

                        else if ( ((LA13_0>='\u0000' && LA13_0<='\b')||(LA13_0>='\u000B' && LA13_0<='\f')||(LA13_0>='\u000E' && LA13_0<='\u001F')||LA13_0=='!'||(LA13_0>='#' && LA13_0<='&')||(LA13_0>='(' && LA13_0<='+')||(LA13_0>=';' && LA13_0<='<')||(LA13_0>='>' && LA13_0<='@')||LA13_0=='\\'||LA13_0=='`'||LA13_0=='|'||(LA13_0>='~' && LA13_0<='\uFFFF')) ) {s = 23;}

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA13_21 = input.LA(1);

                        s = -1;
                        if ( ((LA13_21>='\u0000' && LA13_21<='\uFFFF')) ) {s = 47;}

                        else s = 23;

                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA13_17 = input.LA(1);

                        s = -1;
                        if ( ((LA13_17>='A' && LA13_17<='Z')) ) {s = 46;}

                        else if ( ((LA13_17>='\u0000' && LA13_17<='@')||(LA13_17>='[' && LA13_17<='\uFFFF')) ) {s = 47;}

                        else s = 23;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 13, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}