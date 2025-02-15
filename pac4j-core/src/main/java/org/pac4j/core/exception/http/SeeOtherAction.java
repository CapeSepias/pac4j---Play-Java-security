package org.pac4j.core.exception.http;

import lombok.Getter;
import lombok.ToString;
import org.pac4j.core.context.HttpConstants;

/**
 * A "See Other" HTTP action.
 *
 * @author Jerome Leleu
 * @since 4.0.0
 */
@Getter
@ToString(callSuper = true)
public class SeeOtherAction extends RedirectionAction implements WithLocationAction {

    private static final long serialVersionUID = 6749123580877847389L;
    private final String location;

    public SeeOtherAction(final String location) {
        super(HttpConstants.SEE_OTHER);
        this.location = location;
    }
}
