# nav_eessi_kodeoppgave


Denne "Jobbsøker" serveren (java vs kotlin) leser fra den gitte API-en og svarer med antallet annonser som inneholder hver tekst, gruppert etter uke for de siste 6 månedene.

En antakelse som ble gjort var å kun vurdere fullstendige uker, og dermed avslutte statistikken i begynnelsen av den nåværende uken for å bevare nøyaktigheten til den første og siste uken av statistikken.

Resultatene fra den konsumerte NAV API-en er paginerte og begrenset til 5000 resultater per filter, derfor er spørringene som gjøres fra denne tjenesten batchet per uke, og hver batch kjøres asynkront og parallelt. Hver batch kjører flere forespørsler til API-en, en for hver side. De fullførte kallene for hver uke er historiske data og bør være de samme hver gang.

### Kjøre tjenesten
Maven må være installert, kjør `mvn spring-boot:run` fra rotkatalogen, deretter kan du bruke curl.
```
curl --location 'http://localhost:8080/api/stats'
```

### Bruke tjenesten
Et enkelt endpoint GET /api/stats returnerer en liste over uker i følgende format:

```
{
    "totalProcessingTime": 18,
    "weeks": [
        {
            "weekNumber": 33,
            "javaEntries": 10,
            "kotlinEntries": 0,
            "totalProcessedJobs": 3333
        },
        {
            "weekNumber": 32,
            "javaEntries": 23,
            "kotlinEntries": 9,
            "totalProcessedJobs": 2836
        }
        ...
    ]
}
```

