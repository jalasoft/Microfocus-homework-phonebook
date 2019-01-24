package cz.lastovicka.phonebook.infrastructure.repository.contact.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import cz.lastovicka.phonebook.domain.model.contact.ContactName;

/**
 * Customization of {@link com.thoughtworks.xstream.XStream} parser.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class ContactNameConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
        ContactName name = (ContactName) o;

        hierarchicalStreamWriter.setValue(name.fullName());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        String fullName = hierarchicalStreamReader.getValue();

        return ContactName.fromFullName(fullName);
    }

    @Override
    public boolean canConvert(Class aClass) {
        return ContactName.class.equals(aClass);
    }
}
