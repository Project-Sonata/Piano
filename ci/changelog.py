import sys

def changelog(changed_files):
    print("Hello!")
    print("There files were changed: ")
    print(changed_files)


if __name__ == '__main__':

    changelog(sys.argv[1:])