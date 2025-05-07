import unittest
from user_code import Equipo  # Reemplaza 'tu_modulo' por el nombre del archivo donde esté tu clase

class TestEquipoSinRandom(unittest.TestCase):

    def setUp(self):
        self.equipo1 = Equipo("Leones", 8, 5)
        self.equipo2 = Equipo("Tiburones", 6, 7)
        self.equipo3 = Equipo("Águilas", 7, 7)  # Para probar empate

    def test_gana_equipo_que_llama_jugar_contra(self):
        # 8 (ataque de Leones) - 7 (defensa de Tiburones) = 1 → Leones gana
        resultado = self.equipo1.jugar_contra(self.equipo2)
        self.assertEqual(resultado, "Leones gana contra Tiburones")

    def test_gana_otro_equipo(self):
        # 6 (ataque de Tiburones) - 5 (defensa de Leones) = 1 → Tiburones gana
        resultado = self.equipo2.jugar_contra(self.equipo1)
        self.assertEqual(resultado, "Tiburones gana contra Leones")

    def test_empate(self):
        # 7 - 7 = 0 → Empate
        resultado = self.equipo3.jugar_contra(self.equipo3)
        self.assertEqual(resultado, "Empate")

if __name__ == '__main__':
    unittest.main()