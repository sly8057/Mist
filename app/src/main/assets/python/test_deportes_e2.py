import unittest
from user_code import calcular_estadisticas

class TestEntrenamiento(unittest.TestCase):

    def test_calculo_correcto(self):
        entrenamiento = [30, 45, 20, 50, 40, 60, 35]
        total, promedio = calcular_estadisticas(entrenamiento)
        self.assertEqual(total, 280)
        self.assertEqual(promedio, 40.0)

    def test_lista_vacia(self):
        entrenamiento = []
        total, promedio = calcular_estadisticas(entrenamiento)
        self.assertEqual(total, 0)
        self.assertEqual(promedio, 0)

    def test_un_solo_dia(self):
        entrenamiento = [100]
        total, promedio = calcular_estadisticas(entrenamiento)
        self.assertEqual(total, 100)
        self.assertEqual(promedio, 100.0)

if __name__ == '__main__':
    unittest.main()