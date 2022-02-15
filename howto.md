# Практика по Git

## Основные действия
1. Сделан форк репозитория `Kotlin-Polytech/KotlinAsFirst2020`
2. Произведено клонирование форка на локальную машину
3. Репозиторий `argraur/KotlinAsFirst2021` добавлен в качестве апстрима `upstream-my`
4. Произведен `fetch` из добавленного апстрима
5. Создана ветка `backport` и произведен переход на неё
6. Произведен пик коммитов из `upstream-my` с `d535f359` по головной коммит апстрима (`FETCH_HEAD`)
7. Репозиторий `vladdenisov/KotlinAsFirst2021` добавлен в качестве второго апстрима `upstream-theirs`
8. Произведен `fetch` из `upstream-theirs`
9. Возврат на ветку `master`
10. Начало слияния веток `master`, `backport` и `upstream-theirs/master` (`FETCH_HEAD`) в `master`
11. Исправление конфликтов слияния
12. Завершение слияния веток, создание коммита слияния
13. Экспорт апстримов в файл `remotes` и коммит изменения
14. Экспорт истории zsh в howto.md, изменение файла и коммит изменения
15. Проверка графа с помощью `gitk`
    
## `zsh history`
 ```
 1728  git clone https://github.com/argraur/KotlinAsFirst2020
 1729  cd KotlinAsFirst2020
 1730  git remote add upstream-my https://github.com/argraur/KotlinAsFirst2021
 1731  git fetch upstream-my
 1741  git checkout -b backport
 1742  git cherry-pick d535f359...FETCH_HEAD
 1744  git remote add upstream-theirs https://github.com/vladdenisov/KotlinAsFirst2021
 1745  git fetch upstream-theirs
 1746  git checkout master
 1747  git merge --help
 1748  git merge backport upstream-theirs/master
 * Resolving merge conflicts in VS Code at this point *
 1749  git status
 1750* grep -inr "<< HEAD"
 1751* grep -inr ">> HEAD"
 1753* git status
 1754* git add -A
 1755* git merge --continue
 1756* git status
 1757* git log
 1763  git remote -v > remotes
 1764  cat remotes
 1765  git status
 1766  git add -A
 1767  git commit -s
 1768  git status
 1769  history > howto.md
 1770  git add -A
 1771  git commit -s
 1772  git push origin master
 ```