package display;

/**
 * The display prompt to use, which determines the selection to make. Similar to DisplayConfiguration, this
 * alters the display of the game.
 */
public enum DisplayPrompt {
    //TODO: ACTOR PROMPT MUST SELECT ONLY ACTORS, NOT EMPTY TILES: QOL
    ACTOR_PROMPT,
    ITEM_PROMPT,
    SKILL_PROMPT,
    TILE_PROMPT,
    DIALOGUE_PROMPT
}
