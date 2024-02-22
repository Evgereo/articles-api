package world.evgereo.articles.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperUtils extends ModelMapper {
    public MapperUtils() {
        super();
        getConfiguration().setSkipNullEnabled(true);
    }
}
