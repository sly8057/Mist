import unittest
from user_code import contar_colores

class TestContarColores(unittest.TestCase):

    def test_colores_repetidos(self):
        colores = ["rojo", "azul", "rojo", "verde", "azul", "azul"]
        resultado = contar_colores(colores)
        esperado = {"rojo": 2, "azul": 3, "verde": 1}
        self.assertEqual(resultado, esperado)

    def test_lista_vacia(self):
        colores = []
        resultado = contar_colores(colores)
        esperado = {}
        self.assertEqual(resultado, esperado)

    def test_colores_unicos(self):
        colores = ["rojo", "verde", "azul"]
        resultado = contar_colores(colores)
        esperado = {"rojo": 1, "verde": 1, "azul": 1}
        self.assertEqual(resultado, esperado)

    def test_colores_igual_nombre(self):
        colores = ["rojo", "rojo", "rojo"]
        resultado = contar_colores(colores)
        esperado = {"rojo": 3}
        self.assertEqual(resultado, esperado)

if __name__ == '__main__':
    unittest.main()
