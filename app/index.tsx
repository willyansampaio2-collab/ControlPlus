import { router } from "expo-router";
import { Text, TouchableOpacity, View } from "react-native";

export default function Home() {
  return (
    <View
      style={{
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
        padding: 24,
      }}
    >
      <Text
        style={{
          fontSize: 32,
          fontWeight: "bold",
          marginBottom: 40,
        }}
      >
        Control+
      </Text>

      <TouchableOpacity
        onPress={() => router.push("/login")}
        style={{
          backgroundColor: "#2563eb",
          padding: 14,
          borderRadius: 8,
          width: "100%",
          marginBottom: 12,
        }}
      >
        <Text
          style={{
            color: "white",
            textAlign: "center",
            fontWeight: "bold",
          }}
        >
          Entrar
        </Text>
      </TouchableOpacity>

      <TouchableOpacity
        onPress={() => router.push("/register")}
        style={{
          backgroundColor: "#16a34a",
          padding: 14,
          borderRadius: 8,
          width: "100%",
        }}
      >
        <Text
          style={{
            color: "white",
            textAlign: "center",
            fontWeight: "bold",
          }}
        >
          Criar Conta
        </Text>
      </TouchableOpacity>
    </View>
  );
}
