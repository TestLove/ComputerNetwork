package annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jnetpcap.packet.format.JFormatter.Priority;

@Target(value= {ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {

	/**
	 * The Enum Property.
	 */
	public enum Property {
		
		/** The CHECK. */
		CHECK,
		
		/** The OFFSET. */
		OFFSET,
		
		/** The LENGTH. */
		LENGTH,
		
		/** The VALUE. */
		VALUE,
		
		/** The DESCRIPTION. */
		DESCRIPTION,
		
		/** The DISPLAY. */
		DISPLAY,
		
		/** The MASK. */
		MASK,
		
		/** The UNITS. */
		UNITS,
	}

	/** An empty string. */
	public final static String EMPTY = "";

	/** Default formatting string for field's value. */
	public final static String DEFAULT_FORMAT = "%s";

	/**
	 * Static offset of this field into the header in bits. This parameter
	 * specifies in bits, the exact offset of the annotated field within the
	 * current header. The value is constant. If offset of the field is not
	 * constant but varies and can only be determined at runtime, then this
	 * parameter should not be used. Instead use a method and mark it with
	 * <code>@Dynamic(Property.OFFSET)</code> annotation.
	 * @return offset into the header in bits
	 */
	int offset() default -1;

	/**
	 * Static length of this field within the header in bits. This parameter
	 * specifies in bits, the exact length of the annotated field within the
	 * current header. The value is constant. If length of the field is not
	 * constant but varies and can only be determined at runtime, then this
	 * parameter should not be used. Instead use a method and mark it with
	 * <code>@Dynamic(Property.LENGTH)</code> annotation.
	 * @return length of the field in bits
	 */
	int length() default -1;

	/**
	 * Name of the field. By default, the name of the field is determined
	 * implicitely by using the name of the method. This parameter allows the name
	 * of the field to be explicitely specified. The name of the field, must be
	 * unique within the same header and acts as a unique ID of the field.
	 * 
	 * @return name of the field
	 */
	String name() default EMPTY;

	/**
	 * Name of the field that will be displayed. The name is used by defaul if
	 * display parameter is not set. Display is only a text string that gets
	 * displayed as the name of the field. The actual content of this parameter
	 * have no baring on the name of the field.
	 * 
	 * @return display string to use as a display for field name
	 */
	String display() default EMPTY;

	/**
	 * A short name of the field to display. Nicname is similar to display
	 * parameter. It does not affect the name of the field and is only used for
	 * display purposes where appropriate.
	 * 
	 * @return short name of the filed
	 */
	String nicname() default EMPTY;

	/**
	 * A formatting string for the value of the field. Default is "%s".
	 * 
	 * @return field's formatting string
	 */
	String format() default DEFAULT_FORMAT;

	/**
	 * Units associated with the value of the field.
	 * 
	 * @return string with the name of the units
	 */
	String units() default EMPTY;

	/**
	 * A short description of the field's value.
	 * 
	 * @return a string with value description
	 */
	String description() default EMPTY;

	/**
	 * Sets the parent field's name and implicitely declares this field to be a
	 * subfield of the parent.
	 * 
	 * @return name of the parent field this sub field is appart of
	 */
	String parent() default EMPTY;

	/**
	 * Sets which bits within the field are significant. The mask is also used in
	 * displaying bitfields, where each set bit is reported as significant and non
	 * significant bits are skipped completely. Default is that all bits within
	 * the length of the field are significant.
	 * 
	 * @return a bit mask which has significant bits set
	 */
	public long mask() default 0xFFFFFFFFFFFFFFFFL;

	/**
	 * A priority this field is assigned which is used in determining which field
	 * to include in output depending on what JFormat.Detail level the user has
	 * selected. Default is <code>Priority.MEDIUM</code>.
	 * 
	 * @return display priority of the field.
	 */
	Priority priority() default Priority.MEDIUM;

}