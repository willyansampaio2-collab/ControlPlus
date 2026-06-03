import {
    SafeAreaView,
    StyleSheet,
    Text,
    TouchableOpacity,
    View,
} from "react-native";

import { Colors } from "../src/styles/colors";

export default function Home() {
  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.logo}>Control+</Text>

      <Text style={styles.subtitle}>Continue firme hoje,</Text>

      <Text style={styles.highlight}>você no controle.</Text>

      <View style={styles.progressCard}>
        <Text style={styles.progressLabel}>DIAS LIVRES</Text>

        <Text style={styles.days}>127</Text>

        <Text style={styles.progressFooter}>SEM O VÍCIO</Text>
      </View>

      <Text style={styles.quote}>Pequenas escolhas diárias</Text>

      <Text style={styles.quote}>transformam destinos.</Text>

      <TouchableOpacity style={styles.button}>
        <Text style={styles.buttonText}>Registrar Progresso</Text>
      </TouchableOpacity>

      <View style={styles.focusCard}>
        <Text style={styles.focusTitle}>FOCO DE HOJE</Text>

        <Text style={styles.focusText}>Escolhas melhores,</Text>

        <Text style={styles.focusText}>mente mais forte.</Text>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.background,
    padding: 24,
  },

  logo: {
    color: Colors.primary,
    fontSize: 36,
    fontWeight: "bold",
    textAlign: "center",
    marginTop: 40,
  },

  subtitle: {
    color: Colors.textSecondary,
    textAlign: "center",
    fontSize: 24,
    marginTop: 30,
  },

  highlight: {
    color: Colors.primary,
    fontSize: 34,
    textAlign: "center",
    fontWeight: "bold",
    marginBottom: 40,
  },

  progressCard: {
    backgroundColor: Colors.card,
    borderRadius: 30,
    padding: 40,
    alignItems: "center",
  },

  progressLabel: {
    color: Colors.textSecondary,
    letterSpacing: 2,
  },

  days: {
    color: Colors.text,
    fontSize: 80,
    fontWeight: "bold",
    marginVertical: 10,
  },

  progressFooter: {
    color: Colors.primary,
    fontSize: 18,
    fontWeight: "600",
  },

  quote: {
    color: Colors.textSecondary,
    textAlign: "center",
    fontSize: 18,
    marginTop: 20,
  },

  button: {
    backgroundColor: Colors.primary,
    marginTop: 35,
    padding: 20,
    borderRadius: 20,
    alignItems: "center",
  },

  buttonText: {
    color: "#FFF",
    fontWeight: "bold",
    fontSize: 18,
  },

  focusCard: {
    marginTop: 35,
    backgroundColor: Colors.card,
    borderRadius: 25,
    padding: 20,
  },

  focusTitle: {
    color: Colors.primary,
    marginBottom: 15,
    fontWeight: "600",
  },

  focusText: {
    color: Colors.text,
    fontSize: 18,
  },
});
