package com.amazon.alexa.sdk.interfaces.errors;

import com.amazon.alexa.sdk.data.Directive;
import com.amazon.alexa.sdk.interfaces.AvsItem;

/**
 * Created by will on 6/26/2016.
 */

public class AvsResponseException extends AvsItem {
    Directive directive;
    public AvsResponseException(Directive directive) {
        super(null);
        this.directive = directive;
    }

    public Directive getDirective() {
        return directive;
    }
}
