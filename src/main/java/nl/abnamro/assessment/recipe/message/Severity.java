package nl.abnamro.assessment.recipe.message;

import javax.validation.Payload;

/**
 * @author Orhan Polat
 */
public enum Severity {

    info, warning, error, success, regular;

    public final static Severity DEFAULT = Severity.error;

    public class Info implements Payload {

        private Info() {

        }
    }

    public class Error implements Payload {

        private Error() {

        }
    }

    public class Warning implements Payload {
        private Warning() {
        }
    }

    public class Success implements Payload {
        private Success() {
        }
    }

    public class Regular implements Payload {
        private Regular() {
        }
    }
}
