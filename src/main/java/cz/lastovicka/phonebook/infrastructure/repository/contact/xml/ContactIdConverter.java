package cz.lastovicka.phonebook.infrastructure.repository.contact.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import cz.lastovicka.phonebook.domain.model.contact.ContactId;

/**
 * Customization of {@link com.thoughtworks.xstream.XStream} parser.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class ContactIdConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
        ContactId id = (ContactId) o;

        hierarchicalStreamWriter.setValue(id.asString());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        String value = hierarchicalStreamReader.getValue();

        return ContactId.from(value);
    }

    @Override
    public boolean canConvert(Class aClass) {
        return ContactId.class.equals(aClass);
    }
}
