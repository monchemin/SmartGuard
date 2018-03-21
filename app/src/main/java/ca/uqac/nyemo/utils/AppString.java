package ca.uqac.nyemo.utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nyemo on 16/03/2018.
 */

public abstract class AppString {

    public static final String voiceRecordButtonInitText = "Cliquez pour commencer";
    public static final String voiceRecordButtonNextText = "Texte Suivant";
    public static final String voiceRecordButtonDesableText = "Enregistrement Encours";
    public static final String voiceTextInit = "Instruction de d'enregistrement ici!";


    public static String getVoiceText() {
        ArrayList<String> textList = new ArrayList<String>();

        String text1 = "La vie, c'est le passé, le présent et l'avenir. Je connais un homme qui est" +
                " toujours amoureux de trois femmes : celle qu'il a quittée, souvenirs et regrets ;" +
                " celle qu'il possède, satisfactions immédiates ; et celle qu'il aura et qu'il ne " +
                "connaît pas encore, illusions et rêves.";
        String text2 = "Il suffit de si peu de chose, \n" +
                "Un peu de courage si j'ose. \n" +
                "La vie n'est pas toujours facile, \n" +
                "Mais il suffit de redresser la tête, \n" +
                "D'affronter certaines adversités, \n" +
                "Avec beaucoup de sincérité.";
        String text3 = "Il faut dans la vie savoir aussi, \n" +
                "Tendre la main à qui en a besoin, \n" +
                "Sans espérer un retour... ni rien, \n" +
                "Juste se dire que c'était bien.";
        String text4 = "I remember years ago\n" +
                "Someone told me I should take\n" +
                "Caution when it comes to love\n" +
                "I did, I did\n" +
                "And you were strong and I was not\n" +
                "My illusion, my mistake\n" +
                "I was careless, I forgot";
        String text5 = "Le secret pour faire marcher quelque chose est de vouloir que cela marche, et d'être si positif à son sujet qu'il ne peut absolument pas en être autrement.";
        String text6 = "La règle d'or de la conduite est la tolérance mutuelle, " +
                "car nous ne penserons jamais tous de la même façon, nous ne verrons qu'une " +
                "partie de la vérité et sous des angles différents.\n";


        textList.add(text6);
        textList.add(text2);
        textList.add(text3);
        textList.add(text4);
        textList.add(text5);

        return textList.get(new Random().nextInt(textList.size()));

    }

}
