package cecs429.text;

import java.util.HashSet;

public class TokensProcessor implements TokenProcessor{
    // Turns tokens to types
    @Override
    public String processToken(String token) {
        HashSet<String> tokenSet = new HashSet<>();
        // Covers hyphen case
        if (token.contains("-")){
            String [] tokenSplit = token.split("-");
            for (String s : tokenSplit){
                tokenSet.add(s);
            }
            // Adds combined splitted token, i.e, "Hewlett-Packard-Computing" -> "HewlettPackardComputing"
            tokenSet.add(token.replace("-", ""));
        }

        // Removes non-alphanumeric characters at beginning and end, but not middle
        // Removes quotation marks and apostrophes
        for (String s : tokenSet){
            if (!Character.isLetterOrDigit(s.charAt(0))){
                s=s.substring(1,s.length()-1);
            }
            if (!Character.isLetterOrDigit(s.charAt(s.length()-1))){
                s=s.substring(0,s.length()-2);
            }
            if (s.contains("'")){
                s=s.replace("'", "");
            }
            if (s.contains("\"")){
                s=s.replace("\"", "");
            }

        }


        return tokenSet;
    }

    // Normalizes a type into a term using stemming
    public String normalizeType(String type){
        return "";
    }
}
