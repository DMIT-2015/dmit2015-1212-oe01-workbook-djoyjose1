package ca.nait.dmit.domain;

import jakarta.mail.search.SizeTerm;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlbertaCovid19CaseManager {
    private static AlbertaCovid19CaseManager instance;

    @Getter
    private List<AlbertaCovid19Case> albertaCovid19CaseList = new ArrayList<>();

    public static AlbertaCovid19CaseManager getInstance() throws IOException {
        // https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples#thread-safe-singleton
        if(instance == null){
            synchronized (AlbertaCovid19CaseManager.class) {
                if(instance == null){
                    instance = new AlbertaCovid19CaseManager();
                }
            }
        }
        return instance;
    }

    private AlbertaCovid19CaseManager() throws IOException {
        try(var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "/data/covid-19-alberta-statistics-data.csv")))) {
            String lineText;
            // Declare a delimiter that looks for a comma inside a value
            final var DELIMITER = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
            // We can skip the first line since it contains the header columns
            reader.readLine();

            var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while((lineText = reader.readLine()) != null) {
                // Create an object for each row in the file
                AlbertaCovid19Case currentCase = new AlbertaCovid19Case();

                // The -1 limit allows for any number of fields and not discard empty fields
                String[] values = lineText.split(DELIMITER, -1);

                // Columns order of fields:
                // 0: id
                // 1: dateReported
                // 2: ahsZone
                // 3: gender
                // 4: ageGroup
                // 5: caseStatus
                // 6: caseType
                currentCase.setId(Integer.parseInt(values[0].replaceAll("\"", "")));
                currentCase.setDateReported(LocalDate.parse(values[1].replaceAll("\"", ""), dateTimeFormatter));
                currentCase.setAhsZone(values[2].replaceAll("\"", ""));
                currentCase.setGender(values[3].replaceAll("\"", ""));
                currentCase.setAgeGroup(values[4].replaceAll("\"", ""));
                currentCase.setCaseStatus(values[5].replaceAll("\"", ""));
                currentCase.setCaseType(values[6].replaceAll("\"", ""));

                albertaCovid19CaseList.add(currentCase);
            }
        }
    }

    public List<String> findDistinctAhsZone() {
        return albertaCovid19CaseList
                .stream()
//                .map(AlbertaCovid19Case::getAhsZone)
                .map(item -> item.getAhsZone())
                .distinct()
                .filter(item -> !item.isEmpty())
                .sorted()
                .collect(Collectors.toList());
    }

    public long activeCaseCount() {
        return albertaCovid19CaseList
                .stream()
                .filter(item -> item.getCaseStatus().equalsIgnoreCase("Active"))
                .count();
    }

    public long activeCaseCountByAhsZone(String ahsZone) {
        return albertaCovid19CaseList
                .stream()
//                .filter(item -> item.getCaseStatus().equalsIgnoreCase("Active") && item.getAhsZone().equalsIgnoreCase(ahsZone))
                .filter(item -> item.getCaseStatus().equalsIgnoreCase("Active"))
                .filter(item -> item.getAhsZone().equalsIgnoreCase(ahsZone))
                .count();
    }

    public long caseReportedCountByAhsZoneAndDateRange(String ahsZone, LocalDate fromDate, LocalDate toDate) {
        return albertaCovid19CaseList
                .stream()
                .filter(item -> item.getAhsZone().equalsIgnoreCase(ahsZone))
                // isBefore() and isAfter() are not inclusive. That is why reverse logic is used
                .filter(item -> !item.getDateReported().isBefore(fromDate) && !item.getDateReported().isAfter(toDate))
                .count();
    }
}
