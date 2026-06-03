import { Ionicons } from "@expo/vector-icons";
import {
    Image,
    SafeAreaView,
    StyleSheet,
    Text,
    TouchableOpacity,
    View,
} from "react-native";
import ProgressCard from "../src/components/ProgressCard";
import { Colors } from "../src/styles/colors";

export default function Home() {
  return (
    <SafeAreaView style={styles.container}>
      <Image
        source={require("../assets/images/logo.png")}
        style={styles.logo}
      />

      <Text style={styles.subtitle}>Continue firme hoje,</Text>

      <Text style={styles.highlight}>você no controle.</Text>

      <ProgressCard />

      <Text style={styles.quote}>Pequenas escolhas diárias</Text>

      <Text style={styles.quote}>transformam destinos.</Text>

      <TouchableOpacity style={styles.button}>
        <View style={styles.plusCircle}>
          <Ionicons name="add" size={26} color="#FFF" />
        </View>

        <Text style={styles.buttonText}>Registrar Progresso</Text>

        <Ionicons name="chevron-forward" size={24} color="#FFF" />
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
    width: 50,
    height: 50,
    alignSelf: "center",
    marginTop: 10,
    marginBottom: 0,
  },

  subtitle: {
    color: Colors.textSecondary,
    textAlign: "center",
    fontSize: 24,
    marginTop: 10,
  },

  highlight: {
    color: Colors.primary,
    fontSize: 34,
    textAlign: "center",
    fontWeight: "bold",
    marginBottom: 0,
  },

  progressCard: {
    backgroundColor: "#12061F",

    borderRadius: 35,

    paddingVertical: 50,

    alignItems: "center",

    marginTop: 20,

    shadowColor: "#8B5CF6",
    shadowOpacity: 0.5,
    shadowRadius: 25,

    elevation: 12,
  },

  progressLabel: {
    color: Colors.textSecondary,
    letterSpacing: 2,
  },

  days: {
    color: "#FFF",
    fontSize: 95,
    fontWeight: "300",
    marginVertical: 15,
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
    marginTop: 13,
  },

  buttonText: {
    color: "#FFF",
    fontWeight: "bold",
    fontSize: 18,
  },

  focusCard: {
    marginTop: 25,
    backgroundColor: "#12061F",
    borderRadius: 25,
    padding: 25,

    shadowColor: "#7C3AED",
    shadowOpacity: 0.4,
    shadowRadius: 15,

    elevation: 8,
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
  plusCircle: {
    width: 45,
    height: 45,
    borderRadius: 25,
    backgroundColor: "rgba(255,255,255,0.2)",
    justifyContent: "center",
    alignItems: "center",
  },

  button: {
    backgroundColor: "#8B5CF6",

    marginTop: 40,

    paddingVertical: 20,
    paddingHorizontal: 24,

    borderRadius: 25,

    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",

    shadowColor: "#A855F7",
    shadowOpacity: 0.8,
    shadowRadius: 20,

    elevation: 15,
  },
});
