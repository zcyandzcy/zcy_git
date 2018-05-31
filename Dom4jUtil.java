public class Dom4jUtil {

    public static String map2xml(Map<String, String> map,String rootElement) {

        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement(rootElement);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            CDATA cdata = DocumentHelper.createCDATA(entry.getValue());
            root.addElement(entry.getKey()).add(cdata);
        }
        return doc.asXML();
    }

    public static Map<String, String> xml2map(InputStream inputStream) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(inputStream);
            return parseXml(document);
        } catch (DocumentException e) {
            throw new RuntimeException("解析xml错误 xml", e);
        }

    }

    public static Map<String, String> xml2map(String xml) {
        SAXReader reader = new SAXReader();
        StringReader stringReader = new StringReader(xml);
        try {
            Document document = reader.read(stringReader);
            return parseXml(document);
        } catch (DocumentException e) {
            throw new RuntimeException("解析xml错误 xml", e);
        }
    }


    private static Map<String, String> parseXml(Document document) {
        Map<String, String> res = new HashMap<>();


        Element rootElement = document.getRootElement();
        List<Element> content = rootElement.elements();
        if (content != null) {
            for (Element element : content) {
                res.put(element.getName(), element.getText());
            }
        }

        return res;
    }
}