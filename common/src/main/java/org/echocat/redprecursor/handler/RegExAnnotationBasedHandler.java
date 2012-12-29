/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat RedPrecursor, Copyright (c) 2011-2012 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.redprecursor.handler;

import org.echocat.redprecursor.annotations.MatchesPattern;
import org.echocat.redprecursor.annotations.RegEx;
import org.echocat.redprecursor.handler.AnnotationBasedHandler.HandlesAnnotations;

@HandlesAnnotations({
    RegEx.class,
    javax.annotation.RegEx.class,
    MatchesPattern.class,
    javax.annotation.MatchesPattern.class
})
public class RegExAnnotationBasedHandler extends AnnotationBasedHandler {}
