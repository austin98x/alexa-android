package com.amazon.alexa.sdk.interfaces.playbackcontrol;

import com.amazon.alexa.sdk.interfaces.AvsItem;

/**
 * {@link com.amazon.alexa.sdk.data.Directive} to send a play command to any app playing media
 *
 * This directive doesn't seem applicable to mobile applications
 *
 * @author will on 5/31/2016.
 */

public class AvsMediaPlayCommandItem extends AvsItem {
    public AvsMediaPlayCommandItem(String token) {
        super(token);
    }
}
