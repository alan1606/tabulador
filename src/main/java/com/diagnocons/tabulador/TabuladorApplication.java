package com.diagnocons.tabulador;

import com.diagnocons.tabulador.models.ConceptoPrecio;
import com.diagnocons.tabulador.repositories.ConceptoPrecioRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
@Slf4j
public class TabuladorApplication implements CommandLineRunner {


	@Autowired
	private ConceptoPrecioRepository conceptoPrecioRepository;

	@Value("${file.excel}")
	private String filePath;

	public static void main(String[] args) {
		SpringApplication.run(TabuladorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Map<Long, Double> priceMap = readExcelFile(filePath);

		List<Long> idsPrecios = new ArrayList<>(priceMap.keySet());

		List<ConceptoPrecio> conceptosDb = (List<ConceptoPrecio>) conceptoPrecioRepository.findAllById(idsPrecios);

		log.info("Precios excel {}", priceMap.size());
		log.info("Precios db {}", conceptosDb.size() );

		for(var conceptoPrecio: conceptosDb){
			var idConceptoPrecio = conceptoPrecio.getId();
			if(!priceMap.containsKey(idConceptoPrecio)){
				log.info("Saltando concepto {}", idConceptoPrecio);
				continue;
			}
			conceptoPrecio.setPrecio(priceMap.get(idConceptoPrecio));
			conceptoPrecio.setFechaModificacion(LocalDateTime.now());
			log.info("Actualizar concepto precio {}", conceptoPrecio);
		}

		//La comento porque todavía no quiero los cambios en db
		conceptoPrecioRepository.saveAll(conceptosDb);
	}

	public static Map<Long, Double> readExcelFile(String filePath) {
		Map<Long, Double> priceMap = new HashMap<>();

		try (FileInputStream fis = new FileInputStream(filePath);
			 Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheetAt(0); // Obtiene la primera hoja del Excel
			Iterator<Row> rowIterator = sheet.iterator();

			// Saltar la primera fila si es encabezado
			if (rowIterator.hasNext()) {
				rowIterator.next();
			}

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// Lee el valor de id_precio y el precio
				Cell idPrecioCell = row.getCell(1); // Columna B (índice 1)
				Cell precioCell = row.getCell(4);   // Columna E (índice 4)

				if (idPrecioCell != null && precioCell != null) {
					long idPrecio = getNumericCellValueAsLong(idPrecioCell);
					double precio = getNumericCellValueAsDouble(precioCell);

					if (idPrecio != -1 && precio != -1) {
						priceMap.put(idPrecio, precio);
					} else {
						log.warn("Celda inválida en la fila {}: idPrecio={} precio={}", row.getRowNum(), idPrecio, precio);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return priceMap;
	}

	private static long getNumericCellValueAsLong(Cell cell) {
		try {
			if (cell.getCellType() == CellType.NUMERIC.NUMERIC) {
				return (long) cell.getNumericCellValue();
			} else if (cell.getCellType() == CellType.STRING) {
				return Long.parseLong(cell.getStringCellValue());
			}
		} catch (NumberFormatException e) {
			log.error("Error al convertir celda a long: {}", cell.getStringCellValue(), e);
		}
		return -1; // Valor de error
	}

	private static double getNumericCellValueAsDouble(Cell cell) {
		try {
			if (cell.getCellType() == CellType.NUMERIC) {
				return cell.getNumericCellValue();
			} else if (cell.getCellType() == CellType.STRING) {
				return Double.parseDouble(cell.getStringCellValue());
			}
		} catch (NumberFormatException e) {
			log.error("Error al convertir celda a double: {}", cell.getStringCellValue(), e);
		}
		return -1; // Valor de error
	}
}
