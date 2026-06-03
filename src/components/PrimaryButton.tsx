import { LinearGradient } from "expo-linear-gradient";
import { StyleSheet, Text, TouchableOpacity } from "react-native";

export default function PrimaryButton() {
  return (
    <TouchableOpacity>
      <LinearGradient colors={["#A855F7", "#7C3AED"]} style={styles.button}>
        <Text style={styles.text}>Registrar Progresso</Text>
      </LinearGradient>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  button: {
    height: 70,
    borderRadius: 25,

    justifyContent: "center",

    alignItems: "center",
  },

  text: {
    color: "#FFF",

    fontSize: 20,

    fontWeight: "600",
  },
});
