package event;

/**
 * Whenever a flag is fired, it's events can either target the owner of the flag (SELF) or the caster of the
 * event that triggered the flag (SENDER).
 */
public enum FlagRedirectLocation {
    SELF,
    SENDER
}
