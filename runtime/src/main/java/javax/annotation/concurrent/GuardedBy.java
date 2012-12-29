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

package javax.annotation.concurrent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * GuardedBy
 * 
 * The field or method to which this annotation is applied can only be accessed
 * when holding a particular lock, which may be a built-in (synchronization)
 * lock, or may be an explicit java.util.concurrent.Lock.
 * 
 * The argument determines which lock guards the annotated field or method: this :
 * The string literal "this" means that this field is guarded by the class in
 * which it is defined. class-name.this : For inner classes, it may be necessary
 * to disambiguate 'this'; the class-name.this designation allows you to specify
 * which 'this' reference is intended itself : For reference fields only; the
 * object to which the field refers. field-name : The lock object is referenced
 * by the (instance or static) field specified by field-name.
 * class-name.field-name : The lock object is reference by the static field
 * specified by class-name.field-name. method-name() : The lock object is
 * returned by calling the named nil-ary method. class-name.class : The Class
 * object for the specified class should be used as the lock object.
 */
@Target( { ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface GuardedBy {
    String value();
}
