# analytic-hierarchy-process

Program pozwala na utworzenie pliku XML zawierającego opis problemu decyzyjnego według metody Analytic Hierarchy Process.
Aby uruchomić:
```
$ gradle run
```

# instrukcja

1. W pierwszym kroku należy wybrać temat problemu decyzyjnego, wszystkie możliwe alternatywy oraz maksymalny współczynnik CR będący współczynnikiem określającym poziom spójności wyborów dokonywanych przez użytkownika.
2. Następnie należy podać wszystkie istotne kryteria, istnieje możliwość dodawania dowolnej ilości podkryteriów.
3. W kolejnym kroku użytkownik podejmuje wszystkie decyzje, decyzje niespójne podświetlane są na czerwono.
4. W ostatnim kroku cała struktura problemu zapisywana jest do pliku file.xml
