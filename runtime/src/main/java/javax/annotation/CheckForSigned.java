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

package javax.annotation;

import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to annotate a value that may be either negative or nonnegative, and
 * indicates that uses of it should check for
 * negative values before using it in a way that requires the value to be
 * nonnegative, and check for it being nonnegative before using it in a way that
 * requires it to be negative.
 */

@Documented
@TypeQualifierNickname
@Nonnegative(when = When.MAYBE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckForSigned {

}
