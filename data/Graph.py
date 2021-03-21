import sys
import matplotlib.pyplot as plt

def getDataFromFile(file_path):
    file = open(file_path, "r")
    file_lines = file.readlines()
    samples = []
    for sample in file_lines:
        samples.append(int(sample))
    return samples

def main():

    data = []
    total = 0

    for i,arg in enumerate(getDataFromFile(sys.argv[1])):
        if(i % 100 == 0 and i != 0):
            data.append(total/100)
            print(total/100)
            total = 0
        else:
            total += arg
    plt.xlabel('Time')
    plt.ylabel('Amplitude')
    
    plt.plot(data)

    plt.axhline(y=0, color='black')

    plt.legend()
    plt.show()

# Standard boilerplate to call the main() function
if __name__ == '__main__':
    main()
