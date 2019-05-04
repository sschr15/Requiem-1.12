package ladysnake.requiem.api.v1.dialogue;

import com.google.common.collect.ImmutableList;
import ladysnake.requiem.api.v1.annotation.Unlocalized;

public interface CutsceneDialogue {
    void init();

    @Unlocalized String getCurrentText();

    ImmutableList<@Unlocalized String> getCurrentChoices();

    /**
     * Chooses an option in an initialized
     * @param choice the selected choice
     * @throws IllegalArgumentException if the given choice is not part of the {@link #getCurrentChoices() current choices}
     */
    void choose(String choice);
}
