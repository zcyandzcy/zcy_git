public class ExcelUtils {
    @SuppressWarnings("unchecked")
    public static Workbook exportBeanList(List datas, ExportExcelMapping mapping) {
        List<Map<String, Object>> mapList = toListMap(datas);
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();

        //第一行表头
        Row row0 = sheet.createRow(0);
        String[] header = mapping.getHeader();
        for (int i = 0; i < header.length; i++) {
            row0.createCell(i).setCellValue(header[i]);
        }
        String[] columnNames = mapping.getColumnNames();

        for (int i = 0; i < mapList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < columnNames.length; j++) {
                String columnName = columnNames[j];
                columnName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, columnName);
                Object value = mapList.get(i).get(columnName);
                if (Objects.nonNull(value)) {
                    if (value instanceof Number) {
                        row.createCell(j).setCellValue(Double.parseDouble(String.valueOf(value)));
                    } else if (value instanceof LocalDate) {
                        row.createCell(j).setCellValue(DateUtil.LocalDate2Date((LocalDate) value));
                    } else if (value instanceof LocalDateTime) {
                        row.createCell(j).setCellValue(DateUtil.LocalDateTime2Date((LocalDateTime) value));
                    } else {
                        row.createCell(j).setCellValue(String.valueOf(value));
                    }
                }
            }
        }
        return wb;
    }

    private static List toListMap(List<Object> datas) {
        return datas.stream().map(data -> JacksonUtils.getObjectMapper().convertValue(data, Map.class)).collect(Collectors.toList());
    }

    public static void writeOutputStream(String fileName, Workbook workbook) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        String userAgent = request.getHeader("User-Agent");
        byte[] bytes = userAgent.contains("MSIE") ? fileName.getBytes() : fileName.getBytes(StandardCharsets.UTF_8); //处理safari的乱码问题
        String outName = new String(bytes, StandardCharsets.ISO_8859_1); // 各浏览器基本都支持ISO编码
        response.setHeader("Content-disposition", "attachment; filename=" + outName);
        response.setContentType("application/x-xls;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try (OutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            throw new ServiceException(500, "export file exception", ex);
        }
    }
}