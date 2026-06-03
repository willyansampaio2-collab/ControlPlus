import {
  FlatList,
  SafeAreaView,
  StyleSheet,
  Text,
  View,
} from "react-native";

import { Colors } from "../src/styles/colors";

const registros = [
  {
    id: "1",
    titulo: "Recaída registrada",
    data: "02/06/2026",
  },
  {
    id: "2",
    titulo: "7 dias sem recaídas",
    data: "28/05/2026",
  },
  {
    id: "3",
    titulo: "Meta de 30 dias criada",
    data: "20/05/2026",
  },
  {
    id: "4",
    titulo: "14 dias sem recaídas",
    data: "15/05/2026",
  },
];

export default function History() {
  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>Histórico</Text>

      <Text style={styles.subtitle}>
        Acompanhe sua jornada
      </Text>

      <FlatList
        data={registros}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <View style={styles.card}>
            <Text style={styles.cardTitle}>
              {item.titulo}
            </Text>

            <Text style={styles.cardDate}>
              {item.data}
            </Text>
          </View>
        )}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.background,
    padding: 24,
  },

  title: {
    color: Colors.primary,
    fontSize: 34,
    fontWeight: "bold",
    textAlign: "center",
    marginTop: 40,
  },

  subtitle: {
    color: Colors.textSecondary,
    textAlign: "center",
    marginTop: 10,
    marginBottom: 30,
    fontSize: 18,
  },

  card: {
    backgroundColor: Colors.card,
    padding: 20,
    borderRadius: 20,
    marginBottom: 15,
  },

  cardTitle: {
    color: Colors.text,
    fontSize: 18,
    fontWeight: "600",
  },

  cardDate: {
    color: Colors.primary,
    marginTop: 8,
    fontSize: 14,
  },
});