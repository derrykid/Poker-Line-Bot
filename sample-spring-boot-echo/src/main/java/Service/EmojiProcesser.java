package Service;

import com.linecorp.bot.model.message.TextMessage;

import java.util.ArrayList;

public class EmojiProcesser {
    private static final String emojiProductID = "5ac21a18040ab15980c9b43e";
    private static final String spadeID = "70";
    private static final String heartID = "71";
    private static final String cloverID = "72";
    private static final String diamondID = "73";

    private static final TextMessage.Emoji SPADE = TextMessage.Emoji.builder().productId(emojiProductID).emojiId(spadeID).build();
    private static final TextMessage.Emoji HEART = TextMessage.Emoji.builder().productId(emojiProductID).emojiId(heartID).build();
    private static final TextMessage.Emoji CLOVER = TextMessage.Emoji.builder().productId(emojiProductID).emojiId(cloverID).build();
    private static final TextMessage.Emoji DIAMOND = TextMessage.Emoji.builder().productId(emojiProductID).emojiId(diamondID).build();

    public static TextMessage process(String cardDeal) {
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
                    emojis.add(SPADE);
                    break;
                case 'h':
                    emojis.add(HEART);
                    break;
                case 'c':
                    emojis.add(CLOVER);
                    break;
                case 'd':
                    emojis.add(DIAMOND);
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
