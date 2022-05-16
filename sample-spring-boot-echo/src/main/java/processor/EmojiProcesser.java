package processor;

import com.linecorp.bot.model.message.TextMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.NONE)
public class EmojiProcesser {
    private static final String EMOJI_ID = "5ac21a18040ab15980c9b43e";
    private static final String SPADE_ID = "070";
    private static final String HEART_ID = "071";
    private static final String CLOVER_ID = "072";
    private static final String DIAMOND_ID = "073";

    public static TextMessage process(String cardDeal) {
        // TODO change rank "T" to "10"
        /*
        * the String will be in number+char format ex. 2d3cAc
        * replace the 's,h,c,d' with $.
        *
        * Stpes:
        * a. store each char into an arraylist
        * b. according to the char, create the arraylist
        * c. replace the char with $
        * */
        ArrayList<Character> charList = new ArrayList<>();
        for (char e: cardDeal.toCharArray()){
            charList.add(e);
        }

        ArrayList<TextMessage.Emoji> emojis = new ArrayList<>();

       /*
       * index starts with 0, the emoji position mus be at odd number position
       * */
        for(int i = 1; i <= charList.size(); i = i + 2 ){
            switch (charList.get(i)){
                case 's':
                    emojis.add(TextMessage.Emoji.builder().index(i).productId(EMOJI_ID).emojiId(SPADE_ID).build());
                    break;
                case 'h':
                    emojis.add(TextMessage.Emoji.builder().index(i).productId(EMOJI_ID).emojiId(HEART_ID).build());
                    break;
                case 'c':
                    emojis.add(TextMessage.Emoji.builder().index(i).productId(EMOJI_ID).emojiId(CLOVER_ID).build());
                    break;
                case 'd':
                    emojis.add(TextMessage.Emoji.builder().index(i).productId(EMOJI_ID).emojiId(DIAMOND_ID).build());
                    break;
                default:
                    return new TextMessage("Something went wrong in Emoji processor");
            }
        }

        char[] cardDealChar = cardDeal.toCharArray();
        for (int i = 1; i < cardDealChar.length; i = i + 2){
            cardDealChar[i] = '$';
        }

        return TextMessage.builder().text(String.valueOf(cardDealChar)).emojis(emojis).build();


    }
}
