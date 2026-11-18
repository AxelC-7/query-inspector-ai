# query-inspector-ai

Agente inicial de diagnóstico de diferencias contables con Java 21, Spring Boot 3, Oracle JDBC y Gemini API.

## Estado de esta rama

Esta rama incorpora un esqueleto funcional de API y flujo de diagnóstico: inspección de metadatos Oracle, propuesta de consultas SELECT por Gemini, validación heurística, ejecución JDBC limitada y análisis de evidencia.

## Requisitos
- JDK 21 y Maven 3.9+
- Oracle 12c accesible por JDBC
- API key de Gemini

## Configuración
Copia las variables de `.env.example` a tu entorno. Spring Boot no carga archivos `.env` automáticamente. Crea un usuario Oracle exclusivo de auditoría y concede únicamente SELECT sobre los objetos autorizados y permisos mínimos de lectura de metadatos. No concedas DBA, privilegios ANY ni ejecución de paquetes.

## Ejecución

```bash
mvn clean verify
mvn spring-boot:run
```

## API
`POST /api/v1/audits/analyze`

```json
{
  "reportType": "LIBRO_VENTAS",
  "reference": "CIERRE-2026-001",
  "startDate": "2026-01-01",
  "endDate": "2026-01-31",
  "expectedAmount": 150000,
  "actualAmount": 145000,
  "description": "Diferencia entre libro de ventas y reporte contable",
  "schema": "VENTAS"
}
```

## Seguridad y limitaciones

- Hikari `read-only` no reemplaza los privilegios Oracle SELECT-only.
- `ReadOnlySqlGuard` es una barrera heurística, no un parser SQL AST ni un sandbox. No debe considerarse suficiente para producción: hace falta parser compatible con Oracle, allowlist estricta de objetos y columnas, control de coste y pruebas adversariales.
- La allowlist se pasa por ejecución; no se mantiene estado global mutable compartido entre peticiones.
- La extracción de `ALL_SOURCE` se limita a 1500 líneas para evitar contextos ilimitados; debe filtrarse por dependencias y presupuesto de tokens.
- El ejecutor aún no implementa bind variables. No habilitar consultas con parámetros hasta implementarlas.
- El guard heurístico no resuelve de forma completa CTE, subconsultas, sinónimos, database links ni funciones con efectos laterales. No conectar todavía a datos productivos.
- Nunca ejecutar PL/SQL, DML o DDL generado por Gemini.
- Las conclusiones de Gemini son hipótesis respaldadas por evidencia disponible, no certificación contable.

## Próximos pasos
1. Incorporar parser SQL AST compatible con Oracle y pruebas de evasión.
2. Implementar bind variables tipados para fechas e importes.
3. Filtrar extracción PL/SQL por grafo de dependencias y presupuesto de tokens.
4. Persistir auditorías, trazabilidad y evidencias.
5. Añadir pruebas de integración contra una instancia Oracle de prueba.
