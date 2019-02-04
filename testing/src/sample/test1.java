package sample;

        import javafx.application.Application;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.geometry.Insets;
        import javafx.scene.Scene;
        import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
        import javafx.scene.layout.BorderPane;
        import javafx.scene.layout.HBox;
        import javafx.scene.layout.VBox;
        import javafx.scene.text.Font;
        import javafx.scene.text.Text;
        import javafx.stage.Popup;
        import javafx.stage.Stage;
        import sample.Person;

        import java.sql.*;
        import java.util.ArrayList;

public class test1 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private TableView<Person> asset = new TableView<>();
    private ObservableList<Person> data = FXCollections.observableArrayList();
    private ObservableList<String> hdata = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        ArrayList<String> indexlista = new ArrayList();
        ArrayList<String> indexkontor = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivrutin laddad!");
        } catch (ClassNotFoundException e) {
            System.out.println("Misslyckades att ladda drivrutinen");
        }

        BorderPane layout = new BorderPane();
        ComboBox<String> rullista = new ComboBox<>();
        rullista.getItems().addAll("Person", "Hårdvara", "Mjukvara");

        Label rubrik = new Label("Asset Databas");
        Label rubrik2 = new Label("Välj bland listan och sök i databasen eller klicka på \"  Ta Bort \" för att radera vald rad.");
        rubrik2.setFont(new Font(12));
        rubrik2.setPadding(new Insets(10));
        rubrik.setFont(new Font(30));
        rubrik.setPadding(new Insets(10));
        VBox rubriker = new VBox(rubrik, rubrik2);

        HBox hmvara = new HBox();

        hmvara.setSpacing(10);
        Label hrubrik = new Label("Hårdvara");
        Label mrubrik = new Label("Mjukvara");
        hrubrik.setFont(new Font(15));
        mrubrik.setFont(new Font(15));
        hmvara.getChildren().addAll(hrubrik, mrubrik);

        TextField falt1 = new TextField();

        TextField falt2 = new TextField();

        HBox sokbox = new HBox();

        Button sokknapp = new Button("Sök");
        Button laggtillknapp = new Button("Lägg till");
        Button tabortknapp = new Button("Ta bort");
        falt2.setVisible(false);
        sokbox.getChildren().addAll(rullista, falt1, falt2);
        sokbox.setSpacing(10);
        sokbox.setPadding(new Insets(10));

        HBox box2 = new HBox();
        box2.getChildren().addAll(sokknapp, laggtillknapp, tabortknapp, hmvara);
        box2.setSpacing(10);
        box2.setPadding(new Insets(10));
        hmvara.setSpacing(170);
        hmvara.setPadding(new Insets(0, 0, 0, 630));
        hmvara.setVisible(false);

        VBox vbo = new VBox(sokbox, box2);

        layout.setTop(rubriker);

        layout.setLeft(vbo);

        rullista.setOnAction(event -> {
            if (rullista.getSelectionModel().isSelected(0)) {
                falt2.setVisible(true);
                falt1.setPromptText("Förnamn");
                falt2.setPromptText("Efternamn");
                falt1.clear();
                falt2.clear();
                hmvara.setVisible(true);
            } else {
                falt2.setVisible(false);
                falt1.setPromptText("Namn");
                falt1.clear();
                hmvara.setVisible(false);
            }
        });

        HBox table = new HBox();

        TableColumn<Person, String> aid = new TableColumn<>("AnställningsID");
        aid.setMinWidth(150);
        aid.setCellValueFactory(new PropertyValueFactory<>("anställningsId"));

        TableColumn<Person, String> fnamn = new TableColumn<>("FNamn");
        fnamn.setMinWidth(150);
        fnamn.setCellValueFactory(new PropertyValueFactory<>("FNamn"));

        TableColumn<Person, String> enamn = new TableColumn<>("Enamn");
        enamn.setMinWidth(150);
        enamn.setCellValueFactory(new PropertyValueFactory<>("Enamn"));

        TableColumn<Person, String> rnummer = new TableColumn<>("Rumsnummer");
        rnummer.setMinWidth(150);
        rnummer.setCellValueFactory(new PropertyValueFactory<>("Rumsnummer"));

        TableColumn<Person, String> pnummer = new TableColumn<>("Personnummer");
        pnummer.setMinWidth(150);
        pnummer.setCellValueFactory(new PropertyValueFactory<>("Personnummer"));

        TableColumn<Person, String> hvara = new TableColumn<>("hNamn");
        hvara.setMinWidth(150);
        hvara.setCellValueFactory(new PropertyValueFactory<>("hNamn"));

        TableColumn<Person, String> mvara = new TableColumn<>("mNamn");
        mvara.setMinWidth(150);
        mvara.setCellValueFactory(new PropertyValueFactory<>("mNamn"));

        ArrayList<String> hlista = new ArrayList<>();
        ArrayList<String> mlista = new ArrayList<>();
        ListView<String> listahardvara = new ListView();
        ListView<String> listamjukvara = new ListView<>();

        sokknapp.setOnAction(event -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/asset?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "kalle97")) {
                System.out.println("YES!");
                asset.getColumns().clear();
                asset.getItems().clear();

                if (falt1.getText().isEmpty() && falt2.getText().isEmpty()){
                    Alert felMeddelande = new Alert(Alert.AlertType.ERROR);
                    felMeddelande.setTitle("Tomma fält");
                    felMeddelande.setHeaderText("Felmeddelande");
                    felMeddelande.setContentText("Fyll i de tomma fälten för att kunna söka");
                    felMeddelande.show();
                }

                String villkor;

                if (rullista.getSelectionModel().isSelected(0)) {
                    if (falt1.getText().isEmpty() || falt2.getText().isEmpty()) {

                        villkor = "OR";

                    } else {
                        villkor = "AND";
                    }

                    PreparedStatement satementPerson = conn.prepareStatement("select * " +
                            "from person " +
                            "where FNamn = ? " + villkor + " Enamn = ?");
                    satementPerson.setString(1, falt1.getText());
                    satementPerson.setString(2, falt2.getText());

                    ResultSet result = satementPerson.executeQuery();


                    while (result.next()) {
                        Person tmp = new Person(result.getString(1), result.getString(2), result.getString(3), result.getString(4),
                                result.getString(5));


                        data.add(tmp);

                    }

                }

            } catch (SQLException e) {
                System.out.println("Det sekt sig " + e.getMessage());
                throw new NullPointerException();
            }

            asset.setItems(data);
            asset.getColumns().addAll(aid, fnamn, enamn, rnummer, pnummer);

        });

        asset.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/asset?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "kalle97")) {
                System.out.println("YES!");
                hlista.clear();
                mlista.clear();
                listahardvara.getItems().clear();
                listamjukvara.getItems().clear();

                String Fnamnet = asset.getSelectionModel().getSelectedItem().getFNamn();
                String Enamnet = asset.getSelectionModel().getSelectedItem().getEnamn();

                if (Fnamnet == null || Enamnet == null) {
                }

                PreparedStatement statementharvara = conn.prepareStatement("select HNamn from hårdvara join harperson on hårdvara.Serienummer = harperson.Serienummer join person on harperson.AnställningsID = person.AnställningsID where FNamn = ? and Enamn = ?");
                PreparedStatement statementmjukvara = conn.prepareStatement("select mjukvara.MNamn from mjukvara join harmjukvara on mjukvara.MNamn = harmjukvara.MNamn join person on harmjukvara.AnställningsID = person.AnställningsID where FNamn = ? and Enamn = ?");
                statementharvara.setString(1, Fnamnet);
                statementharvara.setString(2, Enamnet);
                statementmjukvara.setString(1, Fnamnet);
                statementmjukvara.setString(2, Enamnet);

                for (int i = 0; i < mlista.size(); i++) {
                    System.out.println(mlista.get(i));
                }

                ResultSet res = statementharvara.executeQuery();
                ResultSet res1 = statementmjukvara.executeQuery();

                while (res.next()) {
                    hlista.add(res.getString(1));
                }
                while (res1.next()) {
                    mlista.add(res1.getString(1));
                }

                listahardvara.getItems().addAll(hlista);
                listamjukvara.getItems().addAll(mlista);

            } catch (SQLException e) {
                System.out.println("Det sekt sig " + e.getMessage());
            }

        });

        tabortknapp.setOnAction(event1 -> {

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/asset?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "kalle97")) {
                System.out.println("YES!");

                if (asset.getSelectionModel().isEmpty()) {
                    Alert fel = new Alert(Alert.AlertType.ERROR);
                    fel.setContentText("Välj person som ska tas bort");
                    fel.show();
                }

                else {

                    String fnamnet = asset.getSelectionModel().getSelectedItem().getFNamn();
                    String enamnet = asset.getSelectionModel().getSelectedItem().getEnamn();
                    String aasntällningsid = asset.getSelectionModel().getSelectedItem().getAnställningsId();

                    System.out.println(fnamnet);
                    System.out.println(enamnet);
                    System.out.println(aasntällningsid);

                    PreparedStatement tabort2 = conn.prepareStatement("DELETE from harmjukvara where AnställningsID = ? ");
                    PreparedStatement tabort3 = conn.prepareStatement("DELETE from harperson where AnställningsID = ?");
                    PreparedStatement tabort = conn.prepareStatement("DELETE from person where FNamn = ? and Enamn = ?");
                    PreparedStatement tabort4 = conn.prepareStatement("DELETE from harlicensnyckel where AnställningsID = ?");

                    tabort4.setString(1, aasntällningsid);
                    tabort2.setString(1, aasntällningsid);
                    tabort3.setString(1, aasntällningsid);
                    tabort.setString(1, fnamnet);
                    tabort.setString(2, enamnet);

                    tabort4.executeUpdate();
                    tabort2.executeUpdate();
                    tabort3.executeUpdate();
                    tabort.executeUpdate();

                    info.setHeaderText("Person borttagen");
                    info.setContentText("Tryck på sök igen för att uppdatera listan");
                    info.showAndWait();

                    fnamnet = null;
                    enamnet = null;
                    aasntällningsid = null;
                }


            } catch (SQLException e) {
                System.out.println("Det sekt sig " + e.getMessage());
            }
        });

        VBox layoutlaggtill = new VBox();
        layoutlaggtill.setPadding(new Insets(10));
        layoutlaggtill.setSpacing(10);
        Stage stagelaggtill = new Stage();
        Scene scenelaggtill = new Scene(layoutlaggtill, 400, 400);

        Label rubriklaggtill = new Label("Välj vad som ska läggas till i databasen");
        rubriklaggtill.setFont(new Font(15));
        ComboBox<String> rullistalaggtill = new ComboBox<>();
        rullistalaggtill.getItems().addAll("Person", "Hårdvara", "Mjukvara");

        TextField text1 = new TextField();
        TextField text2 = new TextField();
        TextField text3 = new TextField();
        TextField text4 = new TextField();
        TextField text5 = new TextField();

        Label kontorLabel = new Label("Rumsnummer: ");
        kontorLabel.setPadding(new Insets(3));
        ComboBox<String> rulllistakontor = new ComboBox<>();
        HBox hboxkontor = new HBox();
        hboxkontor.getChildren().addAll(kontorLabel, rulllistakontor);

        Label rulllistaLabel = new Label();
        rulllistaLabel.setPadding(new Insets(3));
        ComboBox<String> rulllistapersoner = new ComboBox<>();
        HBox hboxrulllista = new HBox();
        hboxrulllista.getChildren().addAll(rulllistaLabel, rulllistapersoner);
        Button laggtill = new Button("Lägg till");

        text1.setVisible(false);
        text2.setVisible(false);
        text3.setVisible(false);
        text4.setVisible(false);
        text5.setVisible(false);
        laggtill.setVisible(false);
        hboxrulllista.setVisible(false);
        hboxkontor.setVisible(false);

        layoutlaggtill.getChildren().addAll(rubriklaggtill,rullistalaggtill,text1, text2, text3, text4, text5,hboxkontor,hboxrulllista, laggtill);

        laggtillknapp.setOnAction(event -> {

            stagelaggtill.setTitle("Lägg till");
            stagelaggtill.setScene(scenelaggtill);
            stagelaggtill.show();

        });

        rullistalaggtill.setOnAction(event1 -> {
            if (rullistalaggtill.getSelectionModel().isSelected(0)){

                hboxkontor.setVisible(true);
                text1.setVisible(true);
                text2.setVisible(true);
                text3.setVisible(true);
                text4.setVisible(true);
                text5.setVisible(false);
                hboxrulllista.setVisible(false);
                laggtill.setVisible(true);

                text1.setPromptText("AnställningsID");
                text2.setPromptText("Förnamn");
                text3.setPromptText("Efternamn");
                text4.setPromptText("Personnummer ÅÅÅÅ-MM-DD");

                rulllistakontor.getItems().clear();
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/asset?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "kalle97")){
                    Statement statementkontor = conn.createStatement();
                    ResultSet resultSetkontor = statementkontor.executeQuery("select Rumsnummer from kontor");
                    indexkontor.clear();
                    while (resultSetkontor.next()){
                        rulllistakontor.getItems().addAll(resultSetkontor.getString(1));
                        indexkontor.add(resultSetkontor.getString(1));
                    }
                }catch (SQLException e){
                    System.out.println("FEL! : " + e.getMessage());
                }

            } else if (rullistalaggtill.getSelectionModel().isSelected(1)){

                hboxkontor.setVisible(false);
                text1.setVisible(true);
                text2.setVisible(true);
                text3.setVisible(true);
                text4.setVisible(true);
                text5.setVisible(false);
                hboxrulllista.setVisible(true);
                laggtill.setVisible(true);

                text1.setPromptText("Typ av Hårdvara");
                text2.setPromptText("Serienummer");
                text3.setPromptText("Hårdvarunamn");
                text4.setPromptText("Märke");

                rulllistaLabel.setText("Personen som mjukvaran tillhör: ");
                rulllistapersoner.getItems().clear();
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/asset?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "kalle97")){
                    Statement statementlista = conn.createStatement();
                    ResultSet resultSetlista = statementlista.executeQuery("select AnställningsID, FNamn, Enamn FROM person");
                    indexlista.clear();
                    while (resultSetlista.next()){
                        rulllistapersoner.getItems().addAll(resultSetlista.getString(1)+ " " + resultSetlista.getString(2) + " " + resultSetlista.getString(3));
                        indexlista.add(resultSetlista.getString(1));
                    }
                }catch (SQLException e){
                    System.out.println("FEL! : " + e.getMessage());
                }

            } else if (rullistalaggtill.getSelectionModel().isSelected(2)){

                hboxkontor.setVisible(false);
                text1.setVisible(true);
                text2.setVisible(true);
                text3.setVisible(true);
                text4.setVisible(false);
                text5.setVisible(false);
                hboxrulllista.setVisible(true);
                laggtill.setVisible(true);

                text1.setPromptText("Version");
                text2.setPromptText("Typ av mjukvara");
                text3.setPromptText("Mjukvarunamn");

                rulllistaLabel.setText("Personen som mjukvaran tillhör: ");
                rulllistapersoner.getItems().clear();
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/asset?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "kalle97")){
                    Statement statementlista = conn.createStatement();
                    ResultSet resultSetlista = statementlista.executeQuery("select AnställningsID, FNamn, Enamn FROM person");
                    indexlista.clear();
                    while (resultSetlista.next()){
                        rulllistapersoner.getItems().addAll(resultSetlista.getString(1)+ " " + resultSetlista.getString(2) + " " + resultSetlista.getString(3));
                        indexlista.add(resultSetlista.getString(1));
                    }
                }catch (SQLException e){
                    System.out.println("FEL! : " + e.getMessage());
                }
            }
        });

        laggtill.setOnAction(event -> {

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/asset?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "kalle97")){

                if (rullistalaggtill.getSelectionModel().getSelectedItem().equals("Person")) {

                    if (text1.getText().isEmpty() || text2.getText().isEmpty() || text3.getText().isEmpty() || text4.getText().isEmpty() || rulllistakontor.getSelectionModel().isEmpty()) {
                        alert.setTitle("Tomma fält");
                        alert.setContentText("Fyll i textfälten och välj rumsnummer");
                        alert.showAndWait();
                    } else {
                        PreparedStatement statementperson = conn.prepareStatement("insert into person VALUES (?, ?, ?, ?, ?)");

                        statementperson.setString(1, text1.getText());
                        statementperson.setString(2, text2.getText());
                        statementperson.setString(3, text3.getText());
                        statementperson.setString(4, indexkontor.get(rulllistakontor.getSelectionModel().getSelectedIndex()));
                        statementperson.setString(5, text4.getText());

                        statementperson.executeUpdate();

                        info.setHeaderText("Person tillagd");
                        info.setContentText("Insättning klar");
                        info.showAndWait();

                        text1.clear();
                        text2.clear();
                        text3.clear();
                        text4.clear();

                    }
                } else if (rullistalaggtill.getSelectionModel().getSelectedItem().equals("Hårdvara")){

                    if (text1.getText().isEmpty() || text2.getText().isEmpty() || text3.getText().isEmpty() || text4.getText().isEmpty() || rulllistapersoner.getSelectionModel().isEmpty()){
                        alert.setTitle("Tomma fält");
                        alert.setContentText("Fyll i textfälten och välj bland personer");
                        alert.showAndWait();
                    } else {
                        PreparedStatement hårdvara = conn.prepareStatement("INSERT INTO hårdvara values (?, ?, ?, ?)");
                        hårdvara.setString(1, text1.getText());
                        hårdvara.setString(2, text2.getText());
                        hårdvara.setString(3, text3.getText());
                        hårdvara.setString(4, text4.getText());
                        PreparedStatement harperson = conn.prepareStatement("INSERT INTO harperson values (?, ?)");
                        harperson.setString(1, text2.getText());
                        harperson.setString(2, indexlista.get(rulllistapersoner.getSelectionModel().getSelectedIndex()));

                        hårdvara.executeUpdate();
                        harperson.executeUpdate();

                        info.setHeaderText("Person Tillagd");
                        info.setContentText("Insättning klar");
                        info.showAndWait();

                        text1.clear();
                        text2.clear();
                        text3.clear();
                        text4.clear();
                        rulllistapersoner.getSelectionModel().clearSelection();

                    }

                } else if (rullistalaggtill.getSelectionModel().getSelectedItem().equals("Mjukvara")){

                    if (text1.getText().isEmpty() || text2.getText().isEmpty() || text3.getText().isEmpty()|| rulllistapersoner.getSelectionModel().isEmpty()){
                        alert.setTitle("Tomma fält");
                        alert.setContentText("Fyll i textfälten och välj bland personer");
                        alert.showAndWait();
                    } else {

                        PreparedStatement mjukvara = conn.prepareStatement("INSERT INTO mjukvara VALUES (?, ?, ?)");
                        mjukvara.setString(1, text1.getText());
                        mjukvara.setString(2, text2.getText());
                        mjukvara.setString(3, text3.getText());
                        PreparedStatement harmjukvara = conn.prepareStatement("INSERT INTO harmjukvara VALUES (?, ?)");
                        harmjukvara.setString(1, text3.getText());
                        harmjukvara.setString(2, indexlista.get(rulllistapersoner.getSelectionModel().getSelectedIndex()));

                        mjukvara.executeUpdate();
                        harmjukvara.executeUpdate();

                        info.setHeaderText("Person Tillagd");
                        info.setContentText("Insättning klar");
                        info.showAndWait();

                        text1.clear();
                        text2.clear();
                        text3.clear();
                        rulllistapersoner.getSelectionModel().clearSelection();
                    }
                }

            }catch (SQLException e){
                System.out.println("FEL! : " + e.getMessage());
            }

        });

        asset.setPrefWidth(765);
        table.getChildren().addAll(asset, listahardvara, listamjukvara);
        layout.setBottom(table);

        Scene scene = new Scene(layout, 1200, 500);
        primaryStage.setTitle("Asset");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}




