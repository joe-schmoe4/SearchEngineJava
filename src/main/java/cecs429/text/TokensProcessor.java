package cecs429.text;

import java.util.ArrayList;
import java.util.List;

public class TokensProcessor implements TokenProcessor{
    // Turns tokens to types
    @Override
    public List<String> processToken(String token) {
        List<String> tokenSet = new ArrayList<>();

        // Covers hyphen case
        if (token.contains("-")){
            String [] tokenSplit = token.split("-");
            for (String s : tokenSplit){
                if (!s.isEmpty()) {
                    tokenSet.add(s);
                }
            }
            // Adds combined splitted token, i.e, "Hewlett-Packard-Computing" -> "HewlettPackardComputing"
            tokenSet.add(token.replace("-", ""));
        }

        else{
            tokenSet.add(token);
        }

        // Removes non-alphanumeric characters at beginning and end, but not middle
        // Removes quotation marks and apostrophes
        // Transforms all tokens to lowercase
        for (int i=0; i<tokenSet.size(); i++){
            String temp = tokenSet.get(i);
            if (!Character.isLetterOrDigit(temp.charAt(0))){
                temp=temp.substring(1,temp.length());
            }
            if (!Character.isLetterOrDigit(temp.charAt(temp.length()-1))){
                temp=temp.substring(0,temp.length()-1);
            }
            if (temp.contains("'")){
                temp=temp.replace("'", "");
            }
            if (temp.contains("\"")){
                temp=temp.replace("\"", "");
            }
            temp = temp.toLowerCase();
            tokenSet.set(i, temp);

        }
        return tokenSet;
    }

    // Normalizes a type into a term using stemming
    public String normalizeType(String type){
        return "";
    }
}
