package encode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.util.ArrayList;
import java.util.List;

//Class to convert From ArrayList to xml String to send it in Soap
public class XmlUtils {

    private static final XmlMapper xmlMapper = new XmlMapper();

    static {
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    /**
     * Converts an ArrayList<FileInfo> to an XML string.
     *
     * @param fileInfos The ArrayList<FileInfo> to convert.
     * @return XML string representing the list.
     * @throws Exception If an error occurs during conversion.
     */
    public static String convertFileInfoListToXml(FileInfoList fileInfoList) throws Exception {
        return xmlMapper.writeValueAsString(fileInfoList);
    }
}
