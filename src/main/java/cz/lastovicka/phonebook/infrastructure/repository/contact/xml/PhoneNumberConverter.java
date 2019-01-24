package cz.lastovicka.phonebook.infrastructure.repository.contact.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import cz.lastovicka.phonebook.domain.model.contact.ContactRepository;
import cz.lastovicka.phonebook.domain.model.contact.ContactsSource;
import cz.lastovicka.phonebook.domain.model.contact.PhoneNumber;

/**
 * Customization of {@link com.thoughtworks.xstream.XStream} parser.
 *
 * @author Jan Lastovicka
 * @since 2019-01-23
 */
public final class PhoneNumberConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
        PhoneNumber number = (PhoneNumber) o;

        hierarchicalStreamWriter.setValue(number.value());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        String value = hierarchicalStreamReader.getValue();

        return PhoneNumber.parse(value);
    }

    @Override
    public boolean canConvert(Class aClass) {
        return PhoneNumber.class.equals(aClass);
    }
}
